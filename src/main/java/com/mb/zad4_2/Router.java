package com.mb.zad4_2;

import com.mb.zad4_2.models.BolidState;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Router {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.TOPIC_WARNING, containerFactory = "topicConnectionFactory")
    public void receiveBolidState(@Payload BolidState state, @Headers MessageHeaders messageHeaders, Message message) throws JMSException {

       String type = message.getJMSType();

       if(Objects.equals(type, "soft")) {
           jmsTemplate.convertAndSend(JmsConfig.QUEUE_WARNING_MECHANICS, state);
       }

       if(Objects.equals(type, "critical")) {
           jmsTemplate.convertAndSend(JmsConfig.QUEUE_WARNING_DRIVER, state);
           jmsTemplate.convertAndSend(JmsConfig.QUEUE_WARNING_MECHANICS, state);
       }

    }
}
