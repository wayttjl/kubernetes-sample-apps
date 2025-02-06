package com.example.functions.echo;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.cloudevents.CloudEvent;
import io.cloudevents.spring.http.CloudEventHttpUtils;

@RestController
public class MathAddController {
    
    @PostMapping("/math/add/123")
    public ResponseEntity<Integer> echo(@RequestBody List<Integer> body, @RequestHeader HttpHeaders headers) {
        CloudEvent attributes = CloudEventHttpUtils.fromHttp(headers) //
                .withId(UUID.randomUUID().toString()) //
                .withSource(URI.create("https://spring.io/foos")) //
                .withType("io.spring.event.Foo") //
                .build();

        Integer output = body.stream().reduce(0, Integer::sum);
        HttpHeaders outgoing = CloudEventHttpUtils.toHttp(attributes);
        return ResponseEntity.ok().headers(outgoing).body(output);
    }
}
