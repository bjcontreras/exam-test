package com.javbre.utilities;

import com.javbre.exception.MissingHeadersException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
@Setter
@Component
public class HeadersUtilities {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final String REGEX_FORMAT = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}";

    private static final List<String> REQUIRED_HEADERS = Arrays.asList(
            HttpHeadersConstants.AUTHORIZATION,
            HttpHeadersConstants.TIMESTAMP,
            HttpHeadersConstants.MSGTYPE
    );

    public static MultiValueMap<String, String> responseHeader(String headerMsgType) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeadersConstants.MSGTYPE, headerMsgType);
        return headers;
    }

    public void validateHeaders(HttpHeaders headers) {
        validateHeadersContent(headers);
        validateTimeStamp(headers);
    }

    private void validateHeadersContent(HttpHeaders headers) {
        for (String header : REQUIRED_HEADERS) {
            if (!headers.containsKey(header)) {
                throw new MissingHeadersException("El header " + header + " es requerido");
            }
        }
    }

    private void validateTimeStamp(HttpHeaders headers) {
        if (!Utilities.timestampsIsValid(headers.getFirst(HttpHeadersConstants.TIMESTAMP))) {
            throw new MissingHeadersException("El formato del timestamp es inválido.");
        }
    }

}
