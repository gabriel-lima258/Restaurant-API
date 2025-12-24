package com.gtech.food_api.domain.filter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailySellingFilter {
    
    private Long restaurantId;

    /*
     * Data inicial para filtro de vendas diárias.
     * Aceita apenas data no formato ISO 8601 (YYYY-MM-DD).
     * Exemplo de uso na URL:
     * GET /reports/daily-selling?creationDateStart=2025-12-18T00:00:00Z
     * A data será convertida para OffsetDateTime no início do dia (00:00:00 UTC)
     * para comparação com o campo createdAt da entidade Order.
     */
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime creationDateStart;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime creationDateEnd;
}
