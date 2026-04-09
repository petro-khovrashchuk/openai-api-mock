package com.fidelix.codingclub.openaiapimock.dto;

import java.util.List;
import lombok.Builder;
import lombok.Singular;

@Builder
public record Response(
    String id,
    String object,
    long created,
    String model,
    @Singular List<Choice> choices,
    Usage usage) {

}
