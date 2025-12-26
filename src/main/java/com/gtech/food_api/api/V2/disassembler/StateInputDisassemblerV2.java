package com.gtech.food_api.api.V2.disassembler;

import com.gtech.food_api.api.V2.dto.input.StateInput;
import com.gtech.food_api.domain.model.State;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateInputDisassemblerV2 {
    
    @Autowired
    private ModelMapper modelMapper;

    public State copyToEntity(StateInput stateInput) {
        return modelMapper.map(stateInput, State.class);
    }

    public void copyToDomainObject(StateInput stateInput, State state) {
        modelMapper.map(stateInput, state);
    }
}
