package com.javbre.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
public class ConvertJsonToYaml {

    private ConvertJsonToYaml() {throw new UnsupportedOperationException("Utility class");}

    public static void convert(String port) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

            URI uri = new URI("http://localhost:" + port + "/v3/api-docs");
            URL url = uri.toURL();
            Object json = jsonMapper.readValue(url, Object.class);
            yamlMapper.writeValue(new File("docs/openapi.yaml"), json);
        } catch (IOException | URISyntaxException e) {
            log.error("Error converting JSON to YAML", e);
        }
    }
}
