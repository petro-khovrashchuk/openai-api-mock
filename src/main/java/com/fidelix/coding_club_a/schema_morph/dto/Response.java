package com.fidelix.coding_club_a.schema_morph.dto;

import java.util.List;

public record Response(
    String id,
    String object,
    long created,
    String model,
    List<Choice> choices,
    Usage usage) {

}
