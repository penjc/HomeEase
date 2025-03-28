package com.jzo2o.market.handler;

import com.jzo2o.market.service.IActivityService;
import com.jzo2o.market.service.ICouponService;
import com.jzo2o.redis.annotations.Lock;
import com.jzo2o.redis.constants.RedisSyncQueueConstants;
import com.jzo2o.redis.sync.SyncManager;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.concurrent.ThreadPoolExecutor;

import static com.jzo2o.market.constants.RedisConstants.Formatter.*;
import static com.jzo2o.market.constants.RedisConstants.RedisKey.COUPON_SEIZE_SYNC_QUEUE_NAME;

@Component
@Slf4j
public class XxlJobHandler {

    @Resource
    private SyncManager syncManager;

    @Resource
    private IActivityService activityService;

    @Resource
    private ICouponService couponService;

    @Resource(name="syncThreadPool")
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 抢券同步队列
     * 10秒一次
     */
    @XxlJob("seizeCouponSyncJob")
    public void seizeCouponSyncJob() {
        //开启十个线程（对应十个队列）
        // for(int index = 0; index < this.redisSyncProperties.getQueueNum(); ++index) {
        //线程的 run方法
        //分布式锁，锁的key对应一个队列
        //从同步队列中获取数据，处理数据，处理完删除对应同步队列中的key
        /**
         * String lockName = "LOCK:" + RedisSyncQueueUtils.getQueueRedisKey(this.queueName, this.index);
         *         RLock lock = this.redissonClient.getLock(lockName);
         *
         *         try {
         *             if (lock.tryLock(0L, -1L, TimeUnit.SECONDS)) {
         *                 List<SyncMessage<T>> data = null;
         *
         *                 while(CollUtils.isNotEmpty(data = this.getData())) {
         *                     this.process(data);
         *
         *                     try {
         *                         Thread.sleep(500L);
         *                     } catch (InterruptedException e) {
         *                         throw new RuntimeException(e);
         *                     }
         *                 }
         *
         *                 return;
         *             }
         *         } catch (Exception var10) {
         *             return;
         *         } finally {
         *             if (lock != null && lock.isLocked()) {
         *                 lock.unlock();
         *             }
         *
         *         }
         */
        syncManager.start(COUPON_SEIZE_SYNC_QUEUE_NAME, RedisSyncQueueConstants.STORAGE_TYPE_HASH, RedisSyncQueueConstants.MODE_SINGLE,threadPoolExecutor);
    }

    /**
     * 活动状态修改，
     * 1.活动进行中状态修改
     * 2.活动已失效状态修改
     * 每分钟执行一次
     */
    @XxlJob("updateActivityStatus")
    public void updateActivitySatus(){
        log.info("定时修改活动状态...");
        try {
            activityService.updateStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 活动预热，整点预热
     *
     */
    @XxlJob("activityPreheat")
    public void activityPreHeat() {
        log.info("优惠券活动定时预热...");
        try {
            activityService.preHeat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 已领取优惠券自动过期任务
     * 每小时执行一次
     */
    @XxlJob("processExpireCoupon")
    public void processExpireCoupon() {
        log.info("已领取优惠券自动过期任务...");
        try {
            couponService.processExpireCoupon();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
