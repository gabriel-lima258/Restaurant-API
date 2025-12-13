package com.gtech.food_api.api.assembler;

import com.gtech.food_api.api.model.StateDTO;
import com.gtech.food_api.domain.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StateDTOAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public StateDTO copyToDTO(State state) {
        return modelMapper.map(state, StateDTO.class);
    }

    public List<StateDTO> toCollectionDTO(List<State> states) {
        return states.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
