package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.UserControllerV2;
import com.gtech.food_api.api.V2.dto.UserDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import com.gtech.food_api.core.security.UsersJwtSecurity;

/**
 * Assembler para converter User em UserDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade User em um DTO UserDTO
 */
@Component
public class UserDTOAssemblerV2 extends RepresentationModelAssemblerSupport<User, UserDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilderV2 linksBuilder;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    public UserDTOAssemblerV2() {
        super(UserControllerV2.class, UserDTO.class);
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

        if (usersJwtSecurity.canViewUsersGroupsPermissions()) {
            userDTO.add(linksBuilder.linkToUsers());
        }
 
        return userDTO;
    }

    @Override
    public CollectionModel<UserDTO> toCollectionModel(Iterable<? extends User> entities) {
        CollectionModel<UserDTO> collectionModel = super.toCollectionModel(entities);
        if (usersJwtSecurity.canViewUsersGroupsPermissions()) {
            collectionModel.add(linksBuilder.linkToUsers());
        }
        return collectionModel;
    }
}
