package com.gtech.food_api.api.V1.dto.report;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DailySelling {
    
    private Date date;
    private Long totalSales;
    private BigDecimal totalBilling;
}
