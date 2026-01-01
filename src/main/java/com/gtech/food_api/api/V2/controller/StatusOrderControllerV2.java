package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.core.security.resource.validations.CheckSecurity;
import com.gtech.food_api.domain.service.StatusOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/orders/{orderCode}")
public class StatusOrderControllerV2 {

    @Autowired
    private StatusOrderService statusOrderService;

    @CheckSecurity.Orders.CanManageOrder
    @PutMapping("/confirm")
    public ResponseEntity<Void> confirm(@PathVariable String orderCode){
        statusOrderService.confirmOrder(orderCode);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Orders.CanManageOrder
    @PutMapping("/deliver")
    public ResponseEntity<Void> deliver(@PathVariable String orderCode){
        statusOrderService.deliverOrder(orderCode);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Orders.CanManageOrder
    @PutMapping("/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String orderCode){
        statusOrderService.cancelOrder(orderCode);
        return ResponseEntity.noContent().build();
    }
}
