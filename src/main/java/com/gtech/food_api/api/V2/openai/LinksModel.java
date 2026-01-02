package com.gtech.food_api.api.V2.openai;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Model to represent HATEOAS links in OpenAPI documentation.
 * Links are represented as a Map where keys are relation names (rel)
 * and values are LinkModel objects with href and templated properties.
 */
@Schema(name = "Links", description = "HATEOAS links for resource navigation")
@Getter
@Setter
public class LinksModel {

    @Schema(description = "Map of link relations to link details")
    private Map<String, LinkModel> links;

    @Schema(name = "Link", description = "HATEOAS link representation")
    @Getter
    @Setter
    public static class LinkModel {
        @Schema(description = "Link URL", example = "http://api.foodapi.com.br/v2/cities/1")
        private String href;
        @Schema(description = "Indicates if the link accepts additional request parameters", example = "false")
        private boolean templated;
    }
}
