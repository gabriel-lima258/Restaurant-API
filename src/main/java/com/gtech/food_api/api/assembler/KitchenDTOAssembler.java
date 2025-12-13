package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.model.KitchenDTO;
import com.gtech.food_api.domain.model.Kitchen;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler para converter Kitchen em KitchenDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Kitchen em um DTO KitchenDTO
 */
@Component
public class KitchenDTOAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public KitchenDTO copyToDTO(Kitchen kitchen) {
        // Kitchen -> KitchenDTO
        return modelMapper.map(kitchen, KitchenDTO.class);
    }

    public List<KitchenDTO> toCollectionDTO(List<Kitchen> kitchens) {
        return kitchens.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
