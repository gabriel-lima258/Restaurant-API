package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "PaymentMethodInput", description = "Payment method input data")
@Getter
@Setter
public class PaymentMethodInput {
    @Schema(description = "Payment method description", example = "Credit Card")
    @NotBlank
    private String description;
    
}
