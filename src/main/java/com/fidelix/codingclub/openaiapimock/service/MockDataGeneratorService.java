package com.fidelix.codingclub.openaiapimock.service;

import tools.jackson.databind.JsonNode;

public interface MockDataGeneratorService {

  String generateContent();

  String generateContentBasedOnSchema(JsonNode schema);
}
