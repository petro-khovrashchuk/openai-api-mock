package com.fidelix.codingclub.openaiapimock.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

@Service
@RequiredArgsConstructor
public class MockDataGeneratorImpl implements MockDataGenerator {

  private final Faker faker;
  private final JsonNodeFactory nodeFactory;

  @Override
  public JsonNode generateData(final JsonNode schema) {
    String type = schema.has("type") ? schema.get("type").stringValue() : "object";

    return switch (type) {
      case "object" -> generateObject(schema.get("properties"));
      case "array" -> generateArray(schema.get("items"));
      case "string" -> nodeFactory.stringNode(faker.lorem().word());
      case "integer", "number" -> nodeFactory.numberNode(faker.number().randomDigit());
      case "boolean" -> nodeFactory.booleanNode(faker.bool().bool());
      default -> nodeFactory.stringNode("mock-value");
    };
  }

  private ObjectNode generateObject(final JsonNode properties) {
    ObjectNode node = nodeFactory.objectNode();
    if (properties != null && properties.isObject()) {
      properties.properties().forEach(entry ->
          node.set(entry.getKey(), generateData(entry.getValue())));
    }
    return node;
  }

  private ArrayNode generateArray(final JsonNode itemsSchema) {
    ArrayNode array = nodeFactory.arrayNode();
    // Generate a random list size between 1 and 3
    int size = faker.number().numberBetween(1, 4);
    for (int i = 0; i < size; i++) {
      array.add(generateData(itemsSchema));
    }
    return array;
  }

}
