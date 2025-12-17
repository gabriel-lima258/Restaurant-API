package com.gtech.food_api.api.controller;

import com.gtech.food_api.domain.service.StatusOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders/{orderId}")
public class StatusOrderController {

    @Autowired
    private StatusOrderService statusOrderService;

    @PutMapping("/confirm")
    public ResponseEntity<Void> confirm(@PathVariable Long orderId){
        statusOrderService.confirmOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/deliver")
    public ResponseEntity<Void> deliver(@PathVariable Long orderId){
        statusOrderService.deliverOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long orderId){
        statusOrderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
