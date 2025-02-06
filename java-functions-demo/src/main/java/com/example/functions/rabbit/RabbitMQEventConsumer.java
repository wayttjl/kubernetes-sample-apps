package com.example.functions.rabbit;

import static org.springframework.cloud.function.cloudevent.CloudEventMessageUtils.ID;
import static org.springframework.cloud.function.cloudevent.CloudEventMessageUtils.SOURCE;
import static org.springframework.cloud.function.cloudevent.CloudEventMessageUtils.SPECVERSION;
import static org.springframework.cloud.function.cloudevent.CloudEventMessageUtils.SUBJECT;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQEventConsumer {

    private static final Counter COUNTER = new Counter();

    private static final Logger LOGGER = Logger.getLogger(RabbitMQEventConsumer.class.getName());

    @PostMapping("/rabbit")
    public ResponseEntity<String> rabbit(@RequestBody String body, @RequestHeader HttpHeaders headers) {
        LOGGER.log(Level.INFO, "Input CE Id:{0}", headers.getFirst(ID));
        LOGGER.log(Level.INFO, "Input CE Spec Version:{0}", headers.getFirst(SPECVERSION));
        LOGGER.log(Level.INFO, "Input CE Source:{0}", headers.getFirst(SOURCE));
        LOGGER.log(Level.INFO, "Input CE Subject:{0}", headers.getFirst(SUBJECT));
        System.out.println(body);
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/rabbit/{cost_second}")
    public ResponseEntity<String> rabbit(@PathVariable("cost_second") int costSecond, @RequestBody String body, @RequestHeader HttpHeaders headers) {
        int tag = new Random().nextInt(100);
        LOGGER.log(Level.INFO, "START,BODY:{0},TIMEOUT:{1},RANDOM_TAG:{2},COUNTER:{3}", new Object[] {body, costSecond, tag, COUNTER.add()});
        try {
            Thread.sleep(costSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "------------------FINISH,BODY:{0},TIMEOUT:{1},RANDOM_TAG:{2},COUNTER:{3}", new Object[] {body, costSecond, tag, COUNTER.remove()});
        return ResponseEntity.ok().body("OK");
    }

    public static int getCount() {
        return COUNTER.get();
    }

    private static class Counter {
        private int count = 0;

        public synchronized int add() {
            count++;
            return count;
        }

        public synchronized int remove() {
            count--;
            return count;
        }

        public int get() {
            return count;
        }
    }
}
