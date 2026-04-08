package com.fidelix.coding_club_a.schema_morph.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.JsonNode;

public record Request(
    String model,
    @JsonProperty("response_format") JsonNode responseFormat) {

}
