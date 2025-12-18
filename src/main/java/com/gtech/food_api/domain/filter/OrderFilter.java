package com.gtech.food_api.domain.filter;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFilter {
    private Long clientId;
    private Long restaurantId;

    // força o formato da data para ISO 8601
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime creationDateStart;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime creationDateEnd;
}
