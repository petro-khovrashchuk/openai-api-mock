package com.fidelix.codingclub.openaiapimock.controller;

import com.fidelix.codingclub.openaiapimock.Constants;
import com.fidelix.codingclub.openaiapimock.dto.Choice;
import com.fidelix.codingclub.openaiapimock.dto.Message;
import com.fidelix.codingclub.openaiapimock.dto.Request;
import com.fidelix.codingclub.openaiapimock.dto.Response;
import com.fidelix.codingclub.openaiapimock.dto.Usage;
import com.fidelix.codingclub.openaiapimock.service.MockDataGeneratorServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping(Constants.V1_CHAT)
@RequiredArgsConstructor
public class Controller {

  private final MockDataGeneratorServiceImpl generator;

  @PostMapping(Constants.COMPLETIONS)
  public Response createMockResponseBasedOnRequest(final @RequestBody Request request) {
    final JsonNode schema = request.responseFormat().at(Constants.JSON_SCHEMA_PATH);

    final JsonNode generatedContent = generator.generateData(schema);

    final String model = request.model();
    return createResponse(model, generatedContent);
  }

  private Response createResponse(final String model, final JsonNode generatedContent) {
    return Response.builder()
        .id(Constants.RESPONSE_ID)
        .object(Constants.RESPONSE_OBJECT)
        .created(Constants.RESPONSE_CREATED)
        .model(model)
        .choice(createChoice(generatedContent))
        .usage(createUsage())
        .build();
  }

  private static @NonNull Choice createChoice(final JsonNode generatedContent) {
    return new Choice(0, createMessage(generatedContent), Constants.CHOICE_FINISH_REASON);
  }

  private static @NonNull Message createMessage(final JsonNode generatedContent) {
    return new Message(Constants.MESSAGE_ROLE, generatedContent.toString());
  }

  private @NonNull Usage createUsage() {
    return new Usage(0, 0, 0);
  }

}

