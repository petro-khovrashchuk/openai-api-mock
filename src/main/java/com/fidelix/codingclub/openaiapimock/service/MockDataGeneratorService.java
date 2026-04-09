package com.fidelix.codingclub.openaiapimock.service;

import tools.jackson.databind.JsonNode;

public interface MockDataGeneratorService {

  JsonNode generateData(JsonNode schema);
}
