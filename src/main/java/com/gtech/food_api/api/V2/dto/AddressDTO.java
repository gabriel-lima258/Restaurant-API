package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Address", description = "Address representation")
@Getter
@Setter
public class AddressDTO {
    @Schema(description = "ZIP code", example = "01310-100")
    private String cep;
    @Schema(description = "Public place (street, avenue, etc.)", example = "Avenida Paulista")
    private String publicPlace;
    @Schema(description = "Address number", example = "1000")
    private String number;
    @Schema(description = "Address complement", example = "Apt 101")
    private String complement;
    @Schema(description = "Neighborhood", example = "Bela Vista")
    private String neighborhood;
    @Schema(description = "City information")
    private CitySummaryDTO city;
}
