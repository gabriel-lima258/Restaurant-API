package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.domain.service.StatusOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders/{orderCode}")
public class StatusOrderController {

    @Autowired
    private StatusOrderService statusOrderService;

    @PutMapping("/confirm")
    public ResponseEntity<Void> confirm(@PathVariable String orderCode){
        statusOrderService.confirmOrder(orderCode);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/deliver")
    public ResponseEntity<Void> deliver(@PathVariable String orderCode){
        statusOrderService.deliverOrder(orderCode);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String orderCode){
        statusOrderService.cancelOrder(orderCode);
        return ResponseEntity.noContent().build();
    }
}
