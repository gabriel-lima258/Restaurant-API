package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.KitchenControllerV2;
import com.gtech.food_api.api.V2.dto.KitchenDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.UsersJwtSecurity;
import com.gtech.food_api.domain.model.Kitchen;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
/**
 * Assembler para converter Kitchen em KitchenDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Kitchen em um DTO KitchenDTO
 */
@Component
public class KitchenDTOAssemblerV2 extends RepresentationModelAssemblerSupport<Kitchen, KitchenDTO> {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private LinksBuilderV2 linksBuilder;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    public KitchenDTOAssemblerV2() {
        super(KitchenControllerV2.class, KitchenDTO.class);
    }
    @Override
    public KitchenDTO toModel(Kitchen kitchen) {
        KitchenDTO kitchenDTO = createModelWithId(kitchen.getId(), kitchen);
        modelMapper.map(kitchen, kitchenDTO);
        
        if (usersJwtSecurity.canViewKitchens()) {
            kitchenDTO.add(linksBuilder.linkToKitchens());
        }
        
        return kitchenDTO;
    }
}
