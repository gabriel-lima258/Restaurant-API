package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.controller.ReportControllerV2.ReportsModel;
import com.gtech.food_api.api.V1.dto.report.DailySelling;
import com.gtech.food_api.domain.filter.DailySellingFilter;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@SecurityRequirement(name = "security_auth")
public interface ReportControllerOpenAi {
    
    ReportsModel reports();

    ResponseEntity<List<DailySelling>> queryDailySelling(DailySellingFilter filter, String timeOffset);

    ResponseEntity<byte[]> queryDailySellingPDF(DailySellingFilter filter, String timeOffset);
    
}

