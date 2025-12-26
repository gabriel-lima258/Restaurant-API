package com.gtech.food_api.api.V1.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.api.V1.dto.input.UserInput;
import com.gtech.food_api.domain.model.User;

/**
 * Disassembler para converter GroupInput em Group
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToDomainObject: converte um GroupInput em uma entidade Group
 */
@Component
public class UserInputDisassemblerV1 {
    
    @Autowired
    private ModelMapper modelMapper;

    public User copyToEntity(UserInput userInput) {
        return modelMapper.map(userInput, User.class);
    }

    public void copyToDomainObject(UserInput userInput, User user) {
        modelMapper.map(userInput, user);
    }
}
