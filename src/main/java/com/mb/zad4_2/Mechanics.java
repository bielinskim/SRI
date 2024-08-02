package com.mb.zad4_2;

import com.mb.zad4_2.models.BolidState;
import jakarta.jms.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Mechanics {

    @JmsListener(destination = JmsConfig.QUEUE_WARNING_MECHANICS, containerFactory = "queueConnectionFactory")
    public void receiveBolidState(@Payload BolidState convertedState, @Headers MessageHeaders messageHeaders, Message message) {
        System.out.println("Mechanics.receiveBolidState, state: "+convertedState);
    }
}
