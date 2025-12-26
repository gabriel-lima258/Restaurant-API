package com.gtech.food_api.api.V1.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityIdInput {
    @NotNull
    private Long id;
}
