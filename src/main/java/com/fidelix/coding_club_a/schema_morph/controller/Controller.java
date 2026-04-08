package com.fidelix.coding_club_a.schema_morph.controller;

import com.fidelix.coding_club_a.schema_morph.dto.Choice;
import com.fidelix.coding_club_a.schema_morph.dto.Message;
import com.fidelix.coding_club_a.schema_morph.dto.Request;
import com.fidelix.coding_club_a.schema_morph.dto.Response;
import com.fidelix.coding_club_a.schema_morph.dto.Usage;
import com.fidelix.coding_club_a.schema_morph.service.MockDataGeneratorImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class Controller {

  private final MockDataGeneratorImpl generator;

  @PostMapping("/completions")
  public Response createMockResponseForSchema(final @RequestBody Request request) {
    final JsonNode schema = request.responseFormat().at("/json_schema/schema");

    final JsonNode generatedContent = generator.generateData(schema);

    final String model = request.model();
    return createResponse(model, generatedContent);
  }

  private Response createResponse(final String model, final JsonNode generatedContent) {
    return new Response(
        "mock-1",
        "chat.completion",
        123,
        model,
        List.of(createChoice(generatedContent)),
        createUsage());
  }

  private static @NonNull Choice createChoice(final JsonNode generatedContent) {
    return new Choice(
        0,
        createMessage(generatedContent),
        "stop");
  }

  private static @NonNull Message createMessage(final JsonNode generatedContent) {
    return new Message(
        "assistant",
        generatedContent.toString());
  }

  private @NonNull Usage createUsage() {
    return new Usage(0, 0, 0);
  }

}

