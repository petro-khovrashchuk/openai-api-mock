package com.fidelix.codingclub.openaiapimock.service;

import tools.jackson.databind.JsonNode;

public interface MockDataGeneratorService {

  String generateString();

  JsonNode generateData(JsonNode schema);
}
