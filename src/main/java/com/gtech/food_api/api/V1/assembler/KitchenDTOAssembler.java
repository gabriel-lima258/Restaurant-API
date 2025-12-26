package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import com.gtech.food_api.api.controller.KitchenController;
import com.gtech.food_api.api.dto.KitchenDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.Kitchen;

/**
 * Assembler para converter Kitchen em KitchenDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Kitchen em um DTO KitchenDTO
 */
@Component
public class KitchenDTOAssembler extends RepresentationModelAssemblerSupport<Kitchen, KitchenDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilder linksBuilder;

    public KitchenDTOAssembler() {
        super(KitchenController.class, KitchenDTO.class);
    }

    @Override
    public KitchenDTO toModel(Kitchen kitchen) {
        KitchenDTO kitchenDTO = createModelWithId(kitchen.getId(), kitchen);
        modelMapper.map(kitchen, kitchenDTO);
        
        kitchenDTO.add(linksBuilder.linkToKitchens());

        return kitchenDTO;
    }
    
    // public KitchenDTO copyToDTO(Kitchen kitchen) {
    //     // Kitchen -> KitchenDTO
    //     return modelMapper.map(kitchen, KitchenDTO.class);
    // }

    // public List<KitchenDTO> toCollectionDTO(Collection<Kitchen> kitchens) {
    //     return kitchens.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
