package com.fidelix.codingclub.openaiapimock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Choice(
    long index,
    Message message,
    @JsonProperty("finish_reason") String finishReason) {

}
