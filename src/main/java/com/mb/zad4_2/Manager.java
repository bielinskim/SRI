package com.mb.zad4_2;


import com.mb.zad4_2.models.SimpleMessage;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Manager {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.QUEUE_DRIVER)
    public void receiveAndRespond(@Payload SimpleMessage convertedMessage, @Headers MessageHeaders headers, Message message) throws JMSException {
        System.out.println("Manager.receiveAndRespond request: " + convertedMessage);

        Destination replyTo = message.getJMSReplyTo();

        SimpleMessage msg = SimpleMessage.builder()
                .id(SimpleMessage.nextId())
                .message("Zgoda")
                .build();
        jmsTemplate.convertAndSend(replyTo, msg);
    }
}
