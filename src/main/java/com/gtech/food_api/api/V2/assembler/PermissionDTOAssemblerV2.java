package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.PermissionControllerV2;
import com.gtech.food_api.api.V2.dto.PermissionDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.UsersJwtSecurity;
import com.gtech.food_api.domain.model.Permission;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
/**
 * Assembler para converter Permission em PermissionDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Permission em um DTO PermissionDTO
 */
@Component
public class PermissionDTOAssemblerV2 extends RepresentationModelAssemblerSupport<Permission, PermissionDTO> {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private LinksBuilderV2 linksBuilder;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    public PermissionDTOAssemblerV2() {
        super(PermissionControllerV2.class, PermissionDTO.class);
    }
    @Override
    public PermissionDTO toModel(Permission permission) {
        PermissionDTO permissionDTO = createModelWithId(permission.getId(), permission);
        modelMapper.map(permission, permissionDTO);
        return permissionDTO;
    }

    @Override
    public CollectionModel<PermissionDTO> toCollectionModel(Iterable<? extends Permission> entities) {
        CollectionModel<PermissionDTO> collectionModel = super.toCollectionModel(entities);
        if (usersJwtSecurity.canViewUsersGroupsPermissions()) {
            collectionModel.add(linksBuilder.linkToPermissions());
        }
        return collectionModel;
    }
}
