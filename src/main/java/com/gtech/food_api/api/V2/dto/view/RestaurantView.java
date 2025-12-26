package com.gtech.food_api.api.V2.dto.view;

/**
 * view é uma classe que define as views possíveis para um objeto
 * exemplo:
 * - Summary: view resumida do restaurante
 * 
 * view é gerenciada pelo Jackson, através de @JsonView atraves do DTO
 * exemplo:
 * 
 * DTO
 * @JsonView(RestaurantView.Summary.class)
 * private String name;
 * 
 * Controller 
 * @JsonView(RestaurantView.Summary.class)
 * public ResponseEntity<List<RestaurantDTO>> listAll(){
 *     List<Restaurant> result = restaurantService.listAll();
 *     List<RestaurantDTO> dtoList = restaurantDTOAssembler.toCollectionDTO(result);
 *     return ResponseEntity.ok().body(dtoList);
 * }
 */
public interface RestaurantView {
    
    public interface Summary {}
}
