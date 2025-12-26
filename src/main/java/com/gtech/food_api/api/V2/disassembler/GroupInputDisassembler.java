package com.gtech.food_api.api.V1.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.domain.model.Group;
import com.gtech.food_api.api.V1.dto.input.GroupInput;

/**
 * Disassembler para converter GroupInput em Group
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToDomainObject: converte um GroupInput em uma entidade Group
 */
@Component
public class GroupInputDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;

    public Group copyToEntity(GroupInput groupInput) {
        return modelMapper.map(groupInput, Group.class);
    }

    public void copyToDomainObject(GroupInput groupInput, Group group) {
        modelMapper.map(groupInput, group);
    }
}
