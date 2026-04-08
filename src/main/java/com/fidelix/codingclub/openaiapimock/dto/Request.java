package com.fidelix.codingclub.openaiapimock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.JsonNode;

public record Request(
    String model,
    @JsonProperty("response_format") JsonNode responseFormat) {

}
