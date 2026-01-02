package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.controller.ReportControllerV2.ReportsModel;
import com.gtech.food_api.api.V1.dto.report.DailySelling;
import com.gtech.food_api.domain.filter.DailySellingFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Reports")
public interface ReportControllerOpenAi {
    
    @Operation(hidden = true)
    ReportsModel reports();

    @Operation(summary = "Get daily selling report (JSON)", description = "Generates a daily selling report in JSON format. Supports filtering by restaurant ID and date range. Time offset is optional (default: +00:00). Example: Get daily selling report for restaurant ID 1 from 2025-01-01 to 2025-01-31.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Daily selling report generated successfully", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DailySelling.class))),
            @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))
        }),
        @ApiResponse(responseCode = "400", description = "Invalid filter parameters", content = @Content(schema = @Schema(ref = "Exceptions")))
    },
    parameters = {
        @Parameter(in = ParameterIn.QUERY, name = "restaurantId", description = "ID of the restaurant", example = "1", schema = @Schema(type = "integer")),
        @Parameter(in = ParameterIn.QUERY, name = "creationDateStart", description = "Initial creation date of the order", example = "2019-12-01T00:00:00Z", schema = @Schema(type = "string", format = "date-time")),
        @Parameter(in = ParameterIn.QUERY, name = "creationDateEnd", description = "Final creation date of the order", example = "2019-12-02T23:59:59Z", schema = @Schema(type = "string", format = "date-time"))
    })
    ResponseEntity<List<DailySelling>> queryDailySelling(
        @Parameter(hidden = true) DailySellingFilter filter,
        @Parameter(description = "Time zone offset (default: +00:00). Example: -03:00 for Brazil timezone", required = false, example = "-03:00") String timeOffset
    );

    @Operation(hidden = true)
    ResponseEntity<byte[]> queryDailySellingPDF(DailySellingFilter filter, String timeOffset);
    
}

