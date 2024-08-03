package com.mb.zad4_2;

import com.mb.zad4_2.models.BolidState;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import jakarta.jms.Message;

@Component
public class Logger {
    @JmsListener(destination = JmsConfig.TOPIC_BOLID, containerFactory = "topicConnectionFactory")
    public void receiveBolidState(@Payload BolidState convertedState, @Headers MessageHeaders messageHeaders, Message message) {
        System.out.println("Logger.receiveBolidState, state: "+convertedState);
    }
}
