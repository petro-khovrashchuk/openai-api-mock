package com.fidelix.codingclub.openaiapimock.service;

import static com.fidelix.codingclub.openaiapimock.Constants.ANY_OF;
import static com.fidelix.codingclub.openaiapimock.Constants.ARRAY;
import static com.fidelix.codingclub.openaiapimock.Constants.BOOLEAN;
import static com.fidelix.codingclub.openaiapimock.Constants.INTEGER;
import static com.fidelix.codingclub.openaiapimock.Constants.ITEMS;
import static com.fidelix.codingclub.openaiapimock.Constants.NUMBER;
import static com.fidelix.codingclub.openaiapimock.Constants.OBJECT;
import static com.fidelix.codingclub.openaiapimock.Constants.ONE_OF;
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
  public String generateContent() {
    return faker.lorem().word();
  }

  @Override
  public String generateContentBasedOnSchema(final JsonNode schema) {
    final JsonNode generateContent = recursivelyGenerateData(schema);
    return generateContent.toString();
  }

  private JsonNode recursivelyGenerateData(final JsonNode node) {
    final String type = parseNodeType(node);

    return switch (type) {
      case OBJECT -> generateObject(node.get(PROPERTIES));
      case ARRAY -> generateArray(node.get(ITEMS));
      case STRING -> nodeFactory.stringNode(faker.lorem().word());
      case INTEGER, NUMBER -> nodeFactory.numberNode(faker.number().randomDigit());
      case BOOLEAN -> nodeFactory.booleanNode(faker.bool().bool());
      case ONE_OF -> recursivelyGenerateData(getFirst(node.get(ONE_OF)));
      case ANY_OF -> recursivelyGenerateData(getFirst(node.get(ANY_OF)));
      default -> nodeFactory.stringNode(Constants.MOCK_VALUE);
    };
  }

  private static String parseNodeType(final JsonNode node) {
    if (node.has(TYPE)) {
      return node.get(TYPE).stringValue();
    } else if (node.has(Constants.ONE_OF) && node.get(Constants.ONE_OF).size() > 0) {
      return ONE_OF;
    } else if (node.has(Constants.ANY_OF) && node.get(Constants.ANY_OF).size() > 0) {
      return ANY_OF;
    } else {
      return OBJECT;
    }
  }

  private static JsonNode getFirst(final JsonNode node) {
    return node.get(0);
  }

  private ObjectNode generateObject(final JsonNode properties) {
    final ObjectNode node = nodeFactory.objectNode();
    if (properties != null && properties.isObject()) {
      properties.properties().forEach(entry ->
          node.set(entry.getKey(), recursivelyGenerateData(entry.getValue())));
    }
    return node;
  }

  private ArrayNode generateArray(final JsonNode itemsSchema) {
    final ArrayNode array = nodeFactory.arrayNode();
    final int size = faker.number().numberBetween(1, 4);
    for (int i = 0; i < size; i++) {
      array.add(recursivelyGenerateData(itemsSchema));
    }
    return array;
  }

}
