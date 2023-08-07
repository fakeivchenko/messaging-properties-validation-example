package com.ivchenko.mpve;

import com.ivchenko.mpve.validation.annotation.PropertiesPattern;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @MessageMapping("validationTest")
    public Mono<String> validationTest(Request request) {
        return Mono.just(request.query);
    }

    public record Request(
            @PropertiesPattern(regexp = "validation.regex", message = "{validation.error.message}")
            String query
    ) {
    }
}
