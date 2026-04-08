package com.fidelix.codingclub.openaiapimock.service;

import tools.jackson.databind.JsonNode;

public interface MockDataGenerator {

  JsonNode generateData(JsonNode schema);
}
