package com.jzo2o.customer.controller.inner;

import com.jzo2o.api.customer.AddressBookApi;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.service.IAddressBookService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner/address-book")
@Api(tags = "内部接口 - 地址簿相关接口")
public class InnerAddressBookController implements AddressBookApi {
    @Resource
    private IAddressBookService addressBookService;

    @Override
    @GetMapping("/{id}")
    public AddressBookResDTO detail(@PathVariable Long id) {
        AddressBook byId = addressBookService.getById(id);
        AddressBookResDTO bean = BeanUtils.toBean(byId, AddressBookResDTO.class);
        return bean;
    }
}
