package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.StateControllerV2;
import com.gtech.food_api.api.V2.dto.StateDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.domain.model.State;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
@Component
public class StateDTOAssemblerV2 extends RepresentationModelAssemblerSupport<State, StateDTO> {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private LinksBuilderV2 linksBuilder;
    public StateDTOAssemblerV2() {
        super(StateControllerV2.class, StateDTO.class);
    }
    @Override
    public StateDTO toModel(State state) {
        StateDTO stateDTO = createModelWithId(state.getId(), state);
        modelMapper.map(state, stateDTO);
        return stateDTO;
    }

    public StateDTO toModelWithSelf(State state) {
        StateDTO stateDTO = toModel(state);
        stateDTO.add(linksBuilder.linkToStates());
        return stateDTO;
    }

    @Override
    public CollectionModel<StateDTO> toCollectionModel(Iterable<? extends State> entities) {
        return super.toCollectionModel(entities)
            .add(linksBuilder.linkToStates());
    }
}
