package com.gtech.food_api.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtech.food_api.api.dto.report.DailySelling;
import com.gtech.food_api.domain.filter.DailySellingFilter;
import com.gtech.food_api.domain.service.SellingQueryService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private SellingQueryService sellingQueryService;

    @GetMapping("/daily-selling")
    public ResponseEntity<List<DailySelling>> queryDailySelling(DailySellingFilter filter) {
        List<DailySelling> dailySelling = sellingQueryService.queryDailySelling(filter);
        return ResponseEntity.ok().body(dailySelling);
    }
    
}
