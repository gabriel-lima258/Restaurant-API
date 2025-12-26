package com.gtech.food_api.api.V1.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;
import com.gtech.food_api.api.V1.controller.CityController;
import com.gtech.food_api.api.V1.dto.CityDTO;
import com.gtech.food_api.api.V1.utils.LinksBuilder;
import com.gtech.food_api.domain.model.City;

/**
 * Assembler responsável por converter entidades City em DTOs CityDTO com links HATEOAS.
 * 
 * Esta classe estende RepresentationModelAssemblerSupport do Spring HATEOAS para facilitar
 * a criação de links RESTful nos DTOs. O HATEOAS (Hypermedia as the Engine of Application State)
 * permite que a API forneça links relacionados aos recursos, tornando a API auto-descritiva
 * e facilitando a navegação entre recursos relacionados.
 * 
 * Funcionalidades:
 * - Converte entidade City para CityDTO usando ModelMapper
 * - Adiciona links HATEOAS ao DTO (self, collection, relacionamentos)
 * - Cria CollectionModel para listas de cidades com links apropriados
 * 
 * Links adicionados:
 * - Self link: Link para o próprio recurso (criado automaticamente por createModelWithId)
 * - Cities link: Link para a coleção de todas as cidades
 * - State link: Link para o estado relacionado à cidade
 * 
 * @Component: Anotação do Spring que registra esta classe como um bean gerenciado pelo container,
 * permitindo injeção de dependência em outras classes.
 */
@Component
public class CityDTOAssembler extends RepresentationModelAssemblerSupport<City, CityDTO>{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilder linksBuilder;

    /**
     * Construtor do assembler que configura o controller e o tipo de DTO.
     * 
     * O RepresentationModelAssemblerSupport precisa conhecer o controller para poder
     * gerar links usando WebMvcLinkBuilder. O controller é usado para construir
     * URIs dos endpoints relacionados ao recurso.
     * 
     * @param CityController.class: Controller que gerencia o recurso City
     * @param CityDTO.class: Tipo do DTO que será retornado
     */
    public CityDTOAssembler() {
        super(CityController.class, CityDTO.class);
    }

    /**
     * Converte uma entidade City em um CityDTO com links HATEOAS.
     * 
     * Este método realiza três operações principais:
     * 1. Cria o DTO base com link self usando createModelWithId()
     * 2. Mapeia os dados da entidade para o DTO usando ModelMapper
     * 3. Adiciona links relacionados (cities collection, state relacionado)
     * 
     * Links adicionados:
     * - Self: Link para o próprio recurso (ex: /cities/1)
     * - Cities: Link para a coleção de cidades (ex: /cities)
     * - State: Link para o estado relacionado (ex: /states/1)
     * 
     * Exemplo de resposta JSON com HATEOAS:
     * ```json
     * {
     *   "id": 1,
     *   "name": "São Paulo",
     *   "state": { "id": 1, "name": "SP", "_links": { "self": { "href": "/states/1" } } },
     *   "_links": {
     *     "self": { "href": "/cities/1" },
     *     "cities": { "href": "/cities" }
     *   }
     * }
     * ```
     * 
     * @param city Entidade City a ser convertida
     * @return CityDTO com dados mapeados e links HATEOAS adicionados
     */
    @Override
    public CityDTO toModel(City city) {
        // Cria o DTO base com link self automaticamente
        CityDTO cityDTO = createModelWithId(city.getId(), city);
        
        // Mapeia os dados da entidade para o DTO
        modelMapper.map(city, cityDTO);

        // Adiciona link self ao estado relacionado
        cityDTO.getState().add(linksBuilder.linkToState(city.getState().getId()));

        return cityDTO;
    }

    // adiciona links all cities somente em caso de findById
    public CityDTO toModelWithSelf(City city) {
        CityDTO cityDTO = toModel(city);

        // Adiciona link para a coleção de cidades
        cityDTO.add(linksBuilder.linkToCities());
        
        return cityDTO;
    }

    /**
     * Converte uma coleção de entidades City em um CollectionModel com links HATEOAS.
     * 
     * O CollectionModel é um wrapper do Spring HATEOAS que envolve uma coleção de recursos
     * e adiciona links relacionados à coleção (como link self da coleção, links de paginação, etc.).
     * 
     * Este método delega para a implementação padrão do RepresentationModelAssemblerSupport,
     * que automaticamente:
     * - Converte cada entidade usando toModel()
     * - Adiciona links apropriados à coleção
     * - Inclui links de navegação se aplicável
     * 
     * @param entities Coleção de entidades City a serem convertidas
     * @return CollectionModel contendo lista de CityDTOs com links HATEOAS
     */
    @Override
    public CollectionModel<CityDTO> toCollectionModel(Iterable<? extends City> entities) {
        return super.toCollectionModel(entities)
            .add(linksBuilder.linkToCities());
    }

    // !metodo padrao DTO sem links HATEOAS
    // public CityDTO copyToDTO(City city) {
    //     return modelMapper.map(city, CityDTO.class);
    // }

    // public List<CityDTO> toCollectionDTO(Collection<City> cities) {
    //     return cities.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
