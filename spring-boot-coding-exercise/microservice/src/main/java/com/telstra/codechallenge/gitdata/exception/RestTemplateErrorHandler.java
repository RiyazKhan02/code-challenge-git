package com.telstra.codechallenge.gitdata.exception;

import com.telstra.codechallenge.gitdata.model.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
@Slf4j
public class RestTemplateErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        log.debug("Status code returned is {}",response.getStatusCode());
        return response.getStatusCode().is4xxClientError() ||
                response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("Status code : {}",response.getStatusCode());
        if(response.getStatusCode().equals(HttpStatus.BAD_REQUEST) ||
        response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
            throw new CustomException(StreamUtils.copyToString(response.getBody(),
                    Charset.defaultCharset()),HttpStatus.valueOf(response.getStatusCode().value()));
        }

    }
}
