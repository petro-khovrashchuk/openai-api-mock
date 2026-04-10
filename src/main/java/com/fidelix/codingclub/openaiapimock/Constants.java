package com.fidelix.codingclub.openaiapimock;

public interface Constants {

  // JSON Schema keywords
  String TYPE = "type";
  String OBJECT = "object";
  String PROPERTIES = "properties";
  String ARRAY = "array";
  String ITEMS = "items";
  String STRING = "string";
  String INTEGER = "integer";
  String NUMBER = "number";
  String BOOLEAN = "boolean";
  String MOCK_VALUE = "mock-value";

  // API endpoints
  String V1_CHAT = "/v1/chat";
  String COMPLETIONS = "/completions";
  String V1_CHAT_COMPLETIONS = V1_CHAT + COMPLETIONS;

  // DTOs constants
  String RESPONSE_ID = "mock-1";
  String RESPONSE_OBJECT = "chat.completion";
  int RESPONSE_CREATED = 123;
  String CHOICE_FINISH_REASON = "stop";
  String MESSAGE_ROLE = "assistant";

  String JSON_SCHEMA_PATH = "/json_schema/schema";
}
