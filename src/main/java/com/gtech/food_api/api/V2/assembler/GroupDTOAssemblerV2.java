package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.GroupControllerV2;
import com.gtech.food_api.api.V2.dto.GroupDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.resource.validations.UsersJwtSecurity;
import com.gtech.food_api.domain.model.Group;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
/**
 * Assembler para converter Group em GroupDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Group em um DTO GroupDTO
 */
@Component
public class GroupDTOAssemblerV2 extends RepresentationModelAssemblerSupport<Group, GroupDTO> {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private LinksBuilderV2 linksBuilder;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    public GroupDTOAssemblerV2() {
        super(GroupControllerV2.class, GroupDTO.class);
    }
    @Override
    public GroupDTO toModel(Group group) {
        GroupDTO groupDTO = createModelWithId(group.getId(), group);
        modelMapper.map(group, groupDTO);
        if (usersJwtSecurity.canViewUsersGroupsPermissions()) {
            groupDTO.add(linksBuilder.linkToGroupPermissions(group.getId()));
        }
        return groupDTO;
    }

    public GroupDTO toModelWithSelf(Group group) {
        GroupDTO groupDTO = toModel(group);
        groupDTO.add(linksBuilder.linkToGroups());
        return groupDTO;
    }

    @Override
    public CollectionModel<GroupDTO> toCollectionModel(Iterable<? extends Group> entities) {
        CollectionModel<GroupDTO> collectionModel = super.toCollectionModel(entities);
        if (usersJwtSecurity.canViewUsersGroupsPermissions()) {
            collectionModel.add(linksBuilder.linkToGroups());
        }
        return collectionModel;
    }
}
