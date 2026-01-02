package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.controller.ReportControllerV2.ReportsModel;
import com.gtech.food_api.api.V1.dto.report.DailySelling;
import com.gtech.food_api.domain.filter.DailySellingFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Reports")
public interface ReportControllerOpenAi {
    
    @Operation(summary = "List available reports", description = "Retrieves a list of all available reports with links to access them. Example: Get the reports entry point to see available report types like daily selling reports.")
    ReportsModel reports();

    @Operation(summary = "Get daily selling report (JSON)", description = "Generates a daily selling report in JSON format. Supports filtering by restaurant ID and date range. Time offset is optional (default: +00:00). Example: Get daily selling report for restaurant ID 1 from 2025-01-01 to 2025-01-31.")
    ResponseEntity<List<DailySelling>> queryDailySelling(DailySellingFilter filter, String timeOffset);

    @Operation(summary = "Get daily selling report (PDF)", description = "Generates a daily selling report in PDF format for download. Supports filtering by restaurant ID and date range. Time offset is optional (default: +00:00). Example: Download PDF report for restaurant ID 1 from 2025-01-01 to 2025-01-31.")
    ResponseEntity<byte[]> queryDailySellingPDF(DailySellingFilter filter, String timeOffset);
    
}

