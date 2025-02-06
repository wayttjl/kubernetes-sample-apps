package com.example.functions.echo;

import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.function.web.util.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.cloud.function.cloudevent.CloudEventMessageUtils.*;

@Component("echo_result")
public class EchoResultFunction implements Function<Message<String>, Message<String>> {
  private static final Logger LOGGER = Logger.getLogger(
      EchoResultFunction.class.getName());

  @Override
  public Message<String> apply(Message<String> inputMessage) {
    /*
     * YOUR CODE HERE
     *
     * Try running `mvn test`. Add more test as you code in
     * `test/java/echo/SpringCloudEventsApplicationTests`.
     */
    HttpHeaders httpHeaders = HeaderUtils.fromMessage(inputMessage.getHeaders());

    LOGGER.log(Level.INFO, "Input CE Id:{0}", httpHeaders.getFirst(ID));
    LOGGER.log(Level.INFO, "Input CE Spec Version:{0}", httpHeaders.getFirst(SPECVERSION));
    LOGGER.log(Level.INFO, "Input CE Source:{0}", httpHeaders.getFirst(SOURCE));
    LOGGER.log(Level.INFO, "Input CE Subject:{0}", httpHeaders.getFirst(SUBJECT));
    String input = "my input is" + inputMessage.getPayload();
    LOGGER.log(Level.INFO, "Input {0} ", input);

    return CloudEventMessageBuilder.withData(input)
        .setType("end").setId(UUID.randomUUID().toString())
        .setSubject("End event")
        .setSource(URI.create("http://example.com/end")).build();
  }
}
