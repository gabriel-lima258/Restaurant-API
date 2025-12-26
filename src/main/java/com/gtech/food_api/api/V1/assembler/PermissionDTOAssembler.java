package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import com.gtech.food_api.api.controller.PermissionController;
import com.gtech.food_api.api.dto.PermissionDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.Permission;

import java.util.stream.Collectors;

/**
 * Assembler para converter Permission em PermissionDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Permission em um DTO PermissionDTO
 */
@Component
public class PermissionDTOAssembler extends RepresentationModelAssemblerSupport<Permission, PermissionDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilder linksBuilder;
    
    public PermissionDTOAssembler() {
        super(PermissionController.class, PermissionDTO.class);
    }

    @Override
    public PermissionDTO toModel(Permission permission) {
        PermissionDTO permissionDTO = createModelWithId(permission.getId(), permission);
        modelMapper.map(permission, permissionDTO);
        return permissionDTO;
    }

    @Override
    public CollectionModel<PermissionDTO> toCollectionModel(Iterable<? extends Permission> entities) {
        return super.toCollectionModel(entities)
            .add(linksBuilder.linkToPermissions());
    }

    // public PermissionDTO copyToDTO(Permission permission) {
    //     // Permission -> PermissionDTO
    //     return modelMapper.map(permission, PermissionDTO.class);
    // }

    // public List<PermissionDTO> toCollectionDTO(Collection<Permission> permissions) {
    //     return permissions.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
