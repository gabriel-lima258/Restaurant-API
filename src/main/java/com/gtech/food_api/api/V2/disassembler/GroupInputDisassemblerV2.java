package com.gtech.food_api.api.V2.disassembler;

import com.gtech.food_api.api.V2.dto.input.GroupInput;
import com.gtech.food_api.domain.model.Group;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Disassembler para converter GroupInput em Group
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToDomainObject: converte um GroupInput em uma entidade Group
 */
@Component
public class GroupInputDisassemblerV2 {
    
    @Autowired
    private ModelMapper modelMapper;

    public Group copyToEntity(GroupInput groupInput) {
        return modelMapper.map(groupInput, Group.class);
    }

    public void copyToDomainObject(GroupInput groupInput, Group group) {
        modelMapper.map(groupInput, group);
    }
}
