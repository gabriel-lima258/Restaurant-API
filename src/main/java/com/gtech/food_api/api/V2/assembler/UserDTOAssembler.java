package com.gtech.food_api.api.V1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.gtech.food_api.api.V1.controller.UserController;
import com.gtech.food_api.api.V1.dto.UserDTO;
import com.gtech.food_api.api.V1.utils.LinksBuilder;
import com.gtech.food_api.domain.model.User;


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

    @Autowired
    private LinksBuilder linksBuilder;
   
    public UserDTOAssembler() {
        super(UserController.class, UserDTO.class);
    }

    @Override
    public UserDTO toModel(User user) {
        UserDTO userDTO = createModelWithId(user.getId(), user);
        modelMapper.map(user, userDTO);

        userDTO.add(linksBuilder.linkToUserGroups(user.getId()));
        return userDTO;
    }

    public UserDTO toModelWithSelf(User user) {
        UserDTO userDTO = toModel(user);

        userDTO.add(linksBuilder.linkToUsers());
 
        return userDTO;
    }

    @Override
    public CollectionModel<UserDTO> toCollectionModel(Iterable<? extends User> entities) {
        return super.toCollectionModel(entities)
            .add(linksBuilder.linkToUsers());
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
