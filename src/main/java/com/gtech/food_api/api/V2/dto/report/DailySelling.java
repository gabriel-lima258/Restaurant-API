package com.gtech.food_api.api.V2.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class DailySelling {
    
    private Date date;
    private Long totalSales;
    private BigDecimal totalBilling;
}
