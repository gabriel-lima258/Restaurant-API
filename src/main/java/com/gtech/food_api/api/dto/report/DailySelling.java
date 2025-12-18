package com.gtech.food_api.api.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DailySelling {
    
    private LocalDate date;
    private Long totalSales;
    private BigDecimal totalBilling;
}
