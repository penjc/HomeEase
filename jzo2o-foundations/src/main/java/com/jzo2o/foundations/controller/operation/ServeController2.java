package com.jzo2o.foundations.controller.operation;

import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("operationServeController2")
@RequestMapping("/operation/serve")
public class ServeController2 {
    public List<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO){
        return null;
    }
}
