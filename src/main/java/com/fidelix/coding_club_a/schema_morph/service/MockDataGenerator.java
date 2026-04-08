package com.fidelix.coding_club_a.schema_morph.service;

import tools.jackson.databind.JsonNode;

public interface MockDataGenerator {

  JsonNode generateData(JsonNode schema);
}
