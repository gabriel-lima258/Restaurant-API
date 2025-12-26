package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import com.gtech.food_api.api.controller.GroupController;
import com.gtech.food_api.api.dto.GroupDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
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
public class GroupDTOAssembler extends RepresentationModelAssemblerSupport<Group, GroupDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilder linksBuilder;

    public GroupDTOAssembler() {
        super(GroupController.class, GroupDTO.class);
    }

    @Override
    public GroupDTO toModel(Group group) {
        GroupDTO groupDTO = createModelWithId(group.getId(), group);
        modelMapper.map(group, groupDTO);

        groupDTO.add(linksBuilder.linkToGroupPermissions(group.getId()));

        return groupDTO;
    }

    public GroupDTO toModelWithSelf(Group group) {
        GroupDTO groupDTO = toModel(group);

        groupDTO.add(linksBuilder.linkToGroups());
 
        return groupDTO;
    }

    @Override
    public CollectionModel<GroupDTO> toCollectionModel(Iterable<? extends Group> entities) {
        return super.toCollectionModel(entities)
            .add(linksBuilder.linkToGroups());
    }

    // public GroupDTO copyToDTO(Group group) {
    //     // Group -> GroupDTO
    //     return modelMapper.map(group, GroupDTO.class);
    // }

    // public List<GroupDTO> toCollectionDTO(Collection<Group> groups) {
    //     return groups.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
