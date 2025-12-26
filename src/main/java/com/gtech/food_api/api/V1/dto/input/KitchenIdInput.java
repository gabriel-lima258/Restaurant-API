package com.gtech.food_api.api.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KitchenIdInput {
    @NotNull
    private Long id;
}
