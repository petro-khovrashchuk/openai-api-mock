package com.fidelix.codingclub.openaiapimock.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidelix.codingclub.openaiapimock.Constants;
import com.fidelix.codingclub.openaiapimock.service.MockDataGeneratorServiceImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

@WebMvcTest
class ControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MockDataGeneratorServiceImpl mockDataGeneratorService;

  @Test
  void createMockResponseBasedOnRequest_whenCalledWithCorrectRequest_shouldReturnOkWithContent()
      throws Exception {
    // Arrange
    when(mockDataGeneratorService.generateContentBasedOnSchema(any()))
        .thenReturn("{\"steps\":\"steps_value\",\"final_answer\":\"final_answer_value\"}");

    // Act & Assert
    mockMvc.perform(post(Constants.V1_CHAT_COMPLETIONS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(readResourceContent("valid-request-formatted.json")))
        .andExpect(status().isOk())
        .andExpect(content().json(readResourceContent("response-formatted.json")));
  }

  private @NonNull String readResourceContent(final String resourceName) throws IOException {
    final File file = ResourceUtils.getFile("classpath:" + resourceName);
    return Files.readString(file.toPath());
  }

  @Test
  void createMockResponseBasedOnRequest_whenCalledWithValidFormattedRequest_shouldReturnOkWithContent()
      throws Exception {
    // Arrange
    when(mockDataGeneratorService.generateContent())
        .thenReturn("content_value");

    // Act & Assert
    mockMvc.perform(post(Constants.V1_CHAT_COMPLETIONS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(readResourceContent("valid-request.json")))
        .andExpect(status().isOk())
        .andExpect(content().json(readResourceContent("response.json")));
  }

}