package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "PaymentMethodIdInput", description = "Payment method ID input")
@Getter
@Setter
public class PaymentMethodIdInput {
    @Schema(description = "Payment method ID", example = "1")
    @NotNull
    private Long id;
    
}
