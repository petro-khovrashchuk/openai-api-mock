package com.fidelix.coding_club_a.schema_morph.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Choice(
    long index,
    Message message,
    @JsonProperty("finish_reason") String finishReason) {

}
