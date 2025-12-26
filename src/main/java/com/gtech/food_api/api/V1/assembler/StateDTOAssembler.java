package com.gtech.food_api.api.assembler;

import com.gtech.food_api.api.controller.StateController;
import com.gtech.food_api.api.dto.StateDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.State;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StateDTOAssembler extends RepresentationModelAssemblerSupport<State, StateDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilder linksBuilder;

    public StateDTOAssembler() {
        super(StateController.class, StateDTO.class);
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

    // public StateDTO copyToDTO(State state) {
    //     return modelMapper.map(state, StateDTO.class);
    // }

    // public List<StateDTO> toCollectionDTO(Collection<State> states) {
    //     return states.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
