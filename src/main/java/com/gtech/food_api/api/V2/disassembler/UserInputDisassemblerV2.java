package com.gtech.food_api.api.V2.disassembler;

import com.gtech.food_api.api.V2.dto.input.UserInput;
import com.gtech.food_api.domain.model.User;
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
public class UserInputDisassemblerV2 {
    
    @Autowired
    private ModelMapper modelMapper;

    public User copyToEntity(UserInput userInput) {
        return modelMapper.map(userInput, User.class);
    }

    public void copyToDomainObject(UserInput userInput, User user) {
        modelMapper.map(userInput, user);
    }
}
