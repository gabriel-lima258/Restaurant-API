package com.gtech.food_api.domain.filter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailySellingFilter {
    
    private Long restaurantId;

    /**
     * Data inicial para filtro de vendas diárias.
     * Aceita apenas data no formato ISO 8601 (YYYY-MM-DD).
     * 
     * Exemplo de uso na URL:
     * GET /reports/daily-selling?creationDateStart=2025-12-18
     * 
     * A data será convertida para OffsetDateTime no início do dia (00:00:00 UTC)
     * para comparação com o campo createdAt da entidade Order.
     */
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate creationDateStart;

    /**
     * Data final para filtro de vendas diárias.
     * Aceita apenas data no formato ISO 8601 (YYYY-MM-DD).
     * 
     * Exemplo de uso na URL:
     * GET /reports/daily-selling?creationDateEnd=2025-12-18
     * 
     * A data será convertida para OffsetDateTime no fim do dia (23:59:59 UTC)
     * para comparação com o campo createdAt da entidade Order.
     */
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate creationDateEnd;
}
