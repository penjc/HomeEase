package com.jzo2o.api.publics;

import com.jzo2o.api.publics.dto.response.LocationResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@FeignClient(contextId = "jzo2o-publics", value = "jzo2o-publics", path = "/publics/inner/map")
public interface MapApi {

    @GetMapping("/getLocationByAddress")
    LocationResDTO getLocationByAddress(@RequestParam("address") String address);
}
