package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;  
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.gtech.food_api.api.controller.GroupController;
import com.gtech.food_api.api.controller.UserController;
import com.gtech.food_api.api.controller.UserGroupController;
import com.gtech.food_api.api.dto.UserDTO;
import com.gtech.food_api.domain.model.User;

import java.util.stream.Collectors;

/**
 * Assembler para converter User em UserDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade User em um DTO UserDTO
 */
@Component
public class UserDTOAssembler extends RepresentationModelAssemblerSupport<User, UserDTO> {

    @Autowired
    private ModelMapper modelMapper;
   
    public UserDTOAssembler() {
        super(UserController.class, UserDTO.class);
    }

    @Override
    public UserDTO toModel(User user) {
        UserDTO userDTO = createModelWithId(user.getId(), user);
        modelMapper.map(user, userDTO);

        userDTO.add(linkTo(methodOn(UserGroupController.class).listAll(user.getId())).withRel("groups-users"));
        return userDTO;
    }

    public UserDTO toModelWithSelf(User user) {
        UserDTO userDTO = toModel(user);

        userDTO.add(linkTo(methodOn(UserController.class).listAll()).withRel("users"));
 
        return userDTO;
    }

    @Override
    public CollectionModel<UserDTO> toCollectionModel(Iterable<? extends User> entities) {
        return super.toCollectionModel(entities)
            .add(linkTo(UserController.class).withSelfRel());
    }

    // public UserDTO copyToDTO(User user) {
    //     // Group -> GroupDTO
    //     return modelMapper.map(user, UserDTO.class);
    // }

    // public List<UserDTO> toCollectionDTO(Collection<User> users) {
    //     return users.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
