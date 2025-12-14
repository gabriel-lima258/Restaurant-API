package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import com.gtech.food_api.api.dto.GroupDTO;
import com.gtech.food_api.domain.model.Group;
import java.util.stream.Collectors;

/**
 * Assembler para converter Group em GroupDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Group em um DTO GroupDTO
 */
@Component
public class GroupDTOAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public GroupDTO copyToDTO(Group group) {
        // Group -> GroupDTO
        return modelMapper.map(group, GroupDTO.class);
    }

    public List<GroupDTO> toCollectionDTO(List<Group> groups) {
        return groups.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
