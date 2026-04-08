package com.fidelix.codingclub.openaiapimock.dto;

import java.util.List;

public record Response(
    String id,
    String object,
    long created,
    String model,
    List<Choice> choices,
    Usage usage) {

}
