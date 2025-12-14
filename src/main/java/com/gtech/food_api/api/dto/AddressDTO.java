package com.gtech.food_api.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private String cep;
    private String publicPlace;
    private String number;
    private String complement;
    private String neighborhood;
    private CitySummaryDTO city;
}
