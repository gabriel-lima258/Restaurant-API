package com.gtech.food_api.api.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.api.model.input.StateInput;
import com.gtech.food_api.domain.model.State;

@Component
public class StateInputDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;

    public State copyToDomainObject(StateInput stateInput) {
        return modelMapper.map(stateInput, State.class);
    }
}
