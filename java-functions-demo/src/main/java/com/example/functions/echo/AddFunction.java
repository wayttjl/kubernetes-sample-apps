package com.example.functions.echo;

import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.function.web.util.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.cloud.function.cloudevent.CloudEventMessageUtils.*;

@Component("add")
public class AddFunction implements Function<Message<String>, Message<String>> {
    private static final Logger LOGGER = Logger.getLogger(AddFunction.class.getName());

    @Override
    public Message<String> apply(Message<String> inputMessage) {
        HttpHeaders httpHeaders = HeaderUtils.fromMessage(inputMessage.getHeaders());
        LOGGER.log(Level.INFO, "Input CE Id:{0}", httpHeaders.getFirst(ID));
        LOGGER.log(Level.INFO, "Input CE Spec Version:{0}", httpHeaders.getFirst(SPECVERSION));
        LOGGER.log(Level.INFO, "Input CE Source:{0}", httpHeaders.getFirst(SOURCE));
        LOGGER.log(Level.INFO, "Input CE Subject:{0}", httpHeaders.getFirst(SUBJECT));
        String input = inputMessage.getPayload();
        List<Integer> values;
        String output;
        try {
            values = new ObjectMapper().readValue(input, new TypeReference<List<Integer>>() {
            });
            output = values.stream().reduce(0, Integer::sum).toString();
        } catch (JsonProcessingException e) {
            output = e.getLocalizedMessage();
        }
        LOGGER.log(Level.INFO, "Input {0} ", input);
        LOGGER.log(Level.INFO, "Output {0} ", output);

        return CloudEventMessageBuilder.withData(output)
                .setType("add_result")
                .setId(UUID.randomUUID().toString())
                .setSubject("Add result event")
                .setSource(URI.create("http://example.com/add_result"))
                .build();
    }
}
