package com.gtech.food_api.api.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends RepresentationModel<UserDTO> {
    private Long id;
    private String name;
    private String email;
}
