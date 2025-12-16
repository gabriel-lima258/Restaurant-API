package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import com.gtech.food_api.api.dto.PermissionDTO;
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
public class PermissionDTOAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public PermissionDTO copyToDTO(Permission permission) {
        // Permission -> PermissionDTO
        return modelMapper.map(permission, PermissionDTO.class);
    }

    public List<PermissionDTO> toCollectionDTO(Collection<Permission> permissions) {
        return permissions.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
