package com.gtech.food_api.api.V2.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DailySelling", description = "Daily selling report representation")
@Getter
@Setter
@AllArgsConstructor
public class DailySelling {
    
    @Schema(description = "Report date", example = "2025-01-01")
    private Date date;
    @Schema(description = "Total number of sales", example = "50")
    private Long totalSales;
    @Schema(description = "Total billing amount", example = "3750.00")
    private BigDecimal totalBilling;
}
