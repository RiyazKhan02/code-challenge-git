package com.telstra.codechallenge.gitdata.exception;

import com.telstra.codechallenge.gitdata.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit.jupiter.SpringExtension;



import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestTemplateErrorHandler.class})
public class RestTemplateErrorHandlerTest {

    @Autowired
    private RestTemplateErrorHandler restTemplateErrorHandler;


    @Test
    void hasError() throws Exception {
      assertNotNull(restTemplateErrorHandler);
      assertTrue(
              restTemplateErrorHandler.hasError(
                    new MockClientHttpResponse("error".getBytes(StandardCharsets.UTF_8),HttpStatus.INTERNAL_SERVER_ERROR)));
        assertTrue(
                restTemplateErrorHandler.hasError(
                        new MockClientHttpResponse("error".getBytes(StandardCharsets.UTF_8),HttpStatus.FORBIDDEN)));


    }

    @Test
    void handleError()  {
        assertNotNull(restTemplateErrorHandler);

        MockClientHttpResponse finalMockClientResponseErrorHandler = new MockClientHttpResponse("error".getBytes(StandardCharsets.UTF_8),HttpStatus.INTERNAL_SERVER_ERROR);
        assertThrows(CustomException.class,() -> restTemplateErrorHandler.handleError(finalMockClientResponseErrorHandler));

         var mockClientResponseErrorHandler1 = new MockClientHttpResponse("error".getBytes(StandardCharsets.UTF_8),HttpStatus.BAD_REQUEST);

        assertThrows(CustomException.class,() -> restTemplateErrorHandler.handleError(mockClientResponseErrorHandler1));


    }

}