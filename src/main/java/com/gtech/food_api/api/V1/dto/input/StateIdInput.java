package com.gtech.food_api.api.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class StateIdInput {
    @NotNull
    private Long id;
    
}
