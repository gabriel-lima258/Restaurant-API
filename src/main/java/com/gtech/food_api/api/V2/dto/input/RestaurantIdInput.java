package com.gtech.food_api.api.V2.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantIdInput {
    @NotNull
    private Long id;
}
