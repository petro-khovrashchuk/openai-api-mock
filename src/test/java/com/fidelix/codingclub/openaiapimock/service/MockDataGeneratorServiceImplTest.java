package com.fidelix.codingclub.openaiapimock.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import net.datafaker.Faker;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContentAssert;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.ResourceUtils;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.JsonNodeFactory;

@SpringBootTest(classes = {MockDataGeneratorServiceImpl.class, Faker.class, JsonNodeFactory.class,
    ObjectMapper.class})
@DirtiesContext
class MockDataGeneratorServiceImplTest {

  private static final String INTEGER_REGEX = "-?\\d+";
  private static final String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";

  @Autowired
  private MockDataGeneratorServiceImpl mockDataGenerator;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void generateContent_whenwhenCalled_returnsNotBlankString() {
    // Arrange & Act
    final String content = mockDataGenerator.generateContent();

    // Assert
    assertThat(content).isNotBlank();
  }

  @Test
  void recursivelyGenerateData_whenCalledWithArrayOfStrings_ShouldReturnMockJsonArrayOfStrings()
      throws Exception {

    // Arrange
    final JsonNode json = createSchema("array-of-strings.json");

    // Act
    final String content = mockDataGenerator.generateContentBasedOnSchema(json);

    // Assert
    final JsonContentAssert assertion = getAssertion(content);
    assertion.hasJsonPath("$");
    assertion.extractingJsonPathArrayValue("$").isNotEmpty();
    assertion.extractingJsonPathArrayValue("$").hasOnlyElementsOfType(String.class);
  }

  private @NonNull JsonNode createSchema(final String resourceName) throws IOException {
    final File file = ResourceUtils.getFile("classpath:" + resourceName);
    final String schema = Files.readString(file.toPath());
    return objectMapper.readTree(schema);
  }

  private @NonNull JsonContentAssert getAssertion(final String content) {
    return new JsonContentAssert(getClass(), content);
  }

  @Test
  void recursivelyGenerateData_whenCalledWithBooleanSchema_shouldReturnMockBoolean()
      throws Exception {

    // Arrange
    final JsonNode json = createSchema("boolean-only.json");

    // Act
    final String content = mockDataGenerator.generateContentBasedOnSchema(json);

    // Assert
    final JsonContentAssert assertion = getAssertion(content);
    assertion.extractingJsonPathBooleanValue("$").isInstanceOf(Boolean.class);
    assertThat(content).containsAnyOf("true", "false");
  }

  @Test
  void recursivelyGenerateData_whenCalledWithIntegerSchema_shouldReturnMockInteger()
      throws Exception {

    // Arrange
    final JsonNode json = createSchema("integer-only.json");

    // Act
    final String content = mockDataGenerator.generateContentBasedOnSchema(json);

    // Assert
    final JsonContentAssert assertion = getAssertion(content);
    assertion.extractingJsonPathNumberValue("$").isInstanceOf(Number.class);
    assertThat(content).matches(INTEGER_REGEX);
  }

  @Test
  void recursivelyGenerateData_whenCalledWithMissingTypeSchema_shouldReturnMockObjectWithProperties()
      throws Exception {

    // Arrange
    final JsonNode json = createSchema("missing-type-defaults-to-object.json");

    // Act
    final String content = mockDataGenerator.generateContentBasedOnSchema(json);

    // Assert
    final JsonContentAssert assertion = getAssertion(content);
    assertion.hasJsonPath("$.fallback");
    assertion.extractingJsonPathStringValue("$.fallback").isInstanceOf(String.class);
    assertion.extractingJsonPathMapValue("$").containsKey("fallback");
  }

  @Test
  void recursivelyGenerateData_whenCalledWithNestedObjectSchema_shouldReturnCorrectStructure()
      throws Exception {

    // Arrange
    final JsonNode json = createSchema("nested-object-array.json");

    // Act
    final String content = mockDataGenerator.generateContentBasedOnSchema(json);

    // Assert
    final JsonContentAssert assertion = getAssertion(content);
    assertion.hasJsonPath("$.user");
    assertion.extractingJsonPathMapValue("$.user").isNotEmpty();
    assertion.hasJsonPath("$.user.id");
    assertion.extractingJsonPathNumberValue("$.user.id").isInstanceOf(Number.class);
    assertion.hasJsonPath("$.user.tags");
    assertion.extractingJsonPathArrayValue("$.user.tags")
        .isInstanceOf(List.class)
        .hasOnlyElementsOfType(String.class);
  }

  @Test
  void recursivelyGenerateData_whenCalledWithNumberSchema_shouldReturnMockNumber()
      throws Exception {

    // Arrange
    final JsonNode json = createSchema("number-only.json");

    // Act
    final String content = mockDataGenerator.generateContentBasedOnSchema(json);

    // Assert
    final JsonContentAssert assertion = getAssertion(content);
    assertion.extractingJsonPathNumberValue("$").isInstanceOf(Number.class);
    assertThat(content).matches(NUMBER_REGEX);
  }

  @Test
  void recursivelyGenerateData_whenCalledWithSimpleObjectSchema_shouldReturnMockObjectWithTypedProperties()
      throws Exception {

    // Arrange
    final JsonNode json = createSchema("object-simple.json");

    // Act
    final String content = mockDataGenerator.generateContentBasedOnSchema(json);

    // Assert
    final JsonContentAssert assertion = getAssertion(content);
    assertion.hasJsonPath("$.name");
    assertion.extractingJsonPathStringValue("$.name").isInstanceOf(String.class);
    assertion.hasJsonPath("$.age");
    assertion.extractingJsonPathNumberValue("$.age").isInstanceOf(Number.class);
    assertion.hasJsonPath("$.active");
    assertion.extractingJsonPathBooleanValue("$.active").isInstanceOf(Boolean.class);
    assertion.extractingJsonPathMapValue("$").containsKeys("name", "age", "active");
  }

  @Test
  void recursivelyGenerateData_whenCalledWithStringSchema_shouldReturnMockString()
      throws Exception {

    // Arrange
    final JsonNode json = createSchema("string-only.json");

    // Act
    final String content = mockDataGenerator.generateContentBasedOnSchema(json);

    // Assert
    final JsonContentAssert assertion = getAssertion(content);
    assertion.extractingJsonPathStringValue("$").isInstanceOf(String.class);
    assertion.extractingJsonPathStringValue("$").isNotEmpty();
  }

  @Test
  void recursivelyGenerateData_whenCalledWithTypeNullSchema_shouldReturnMockValue()
      throws Exception {

    // Arrange
    final JsonNode json = createSchema("unsupported-type-falls-back.json");

    // Act
    final String content = mockDataGenerator.generateContentBasedOnSchema(json);

    // Assert
    final JsonContentAssert assertion = getAssertion(content);
    assertion.hasJsonPath("$");
    assertion.extractingJsonPathStringValue("$").isInstanceOf(String.class);
    assertion.extractingJsonPathValue("$").isEqualTo("mock-value");
  }

}