package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import com.gtech.food_api.api.dto.UserDTO;
import com.gtech.food_api.domain.model.User;

import java.util.stream.Collectors;

/**
 * Assembler para converter User em UserDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade User em um DTO UserDTO
 */
@Component
public class UserDTOAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO copyToDTO(User user) {
        // Group -> GroupDTO
        return modelMapper.map(user, UserDTO.class);
    }

    public List<UserDTO> toCollectionDTO(Collection<User> users) {
        return users.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
