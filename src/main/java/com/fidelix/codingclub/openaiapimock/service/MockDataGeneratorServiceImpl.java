package com.fidelix.codingclub.openaiapimock.service;

import static com.fidelix.codingclub.openaiapimock.Constants.ARRAY;
import static com.fidelix.codingclub.openaiapimock.Constants.BOOLEAN;
import static com.fidelix.codingclub.openaiapimock.Constants.INTEGER;
import static com.fidelix.codingclub.openaiapimock.Constants.ITEMS;
import static com.fidelix.codingclub.openaiapimock.Constants.OBJECT;
import static com.fidelix.codingclub.openaiapimock.Constants.PROPERTIES;
import static com.fidelix.codingclub.openaiapimock.Constants.STRING;
import static com.fidelix.codingclub.openaiapimock.Constants.TYPE;

import com.fidelix.codingclub.openaiapimock.Constants;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

@Service
@RequiredArgsConstructor
public class MockDataGeneratorServiceImpl implements MockDataGeneratorService {

  private final Faker faker;
  private final JsonNodeFactory nodeFactory;

  @Override
  public String generateString() {
    return faker.lorem().word();
  }

  @Override
  public JsonNode generateData(final JsonNode schema) {
    final String type = schema.has(TYPE) ? schema.get(TYPE).stringValue() : OBJECT;

    return switch (type) {
      case OBJECT -> generateObject(schema.get(PROPERTIES));
      case ARRAY -> generateArray(schema.get(ITEMS));
      case STRING -> nodeFactory.stringNode(faker.lorem().word());
      case INTEGER, Constants.NUMBER -> nodeFactory.numberNode(faker.number().randomDigit());
      case BOOLEAN -> nodeFactory.booleanNode(faker.bool().bool());
      default -> nodeFactory.stringNode(Constants.MOCK_VALUE);
    };
  }

  private ObjectNode generateObject(final JsonNode properties) {
    final ObjectNode node = nodeFactory.objectNode();
    if (properties != null && properties.isObject()) {
      properties.properties().forEach(entry ->
          node.set(entry.getKey(), generateData(entry.getValue())));
    }
    return node;
  }

  private ArrayNode generateArray(final JsonNode itemsSchema) {
    final ArrayNode array = nodeFactory.arrayNode();
    // Generate a random list size between 1 and 3
    final int size = faker.number().numberBetween(1, 4);
    for (int i = 0; i < size; i++) {
      array.add(generateData(itemsSchema));
    }
    return array;
  }

}
