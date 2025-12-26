package com.gtech.food_api.api.V2.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethodInput {
    @NotBlank
    private String description;
    
}
