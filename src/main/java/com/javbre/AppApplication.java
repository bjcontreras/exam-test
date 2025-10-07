package com.javbre;

import com.javbre.utilities.ConvertJsonToYaml;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AppApplication.class, args);
        Environment env = context.getEnvironment();
        String profileActive = env.getProperty("spring.profiles.active", "NOT");
        String port = env.getProperty("server.port", "8080");
        if (profileActive.equalsIgnoreCase("local")) {
            ConvertJsonToYaml.convert(port);
        }
    }
}
