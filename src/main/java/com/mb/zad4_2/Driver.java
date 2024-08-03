package com.mb.zad4_2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mb.zad4_2.models.SimpleMessage;
import com.mb.zad4_2.models.BolidState;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Driver {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = JmsConfig.QUEUE_WARNING_DRIVER, containerFactory = "queueConnectionFactory")
    public void receiveBolidState(@Payload BolidState convertedState, @Headers MessageHeaders messageHeaders, Message message) throws JMSException, JsonProcessingException {
        System.out.println("Driver.receiveBolidState, state: "+convertedState);

        SimpleMessage driverMessage = SimpleMessage.builder()
                .id(SimpleMessage.nextId())
                .message("Prośba o zjazd")
                .build();

        TextMessage responseMessage = (TextMessage) jmsTemplate.sendAndReceive(
                JmsConfig.QUEUE_DRIVER, session -> {
                    TextMessage plainMessage = session.createTextMessage();
                    try {
                        plainMessage.setText(objectMapper.writeValueAsString(driverMessage));
                        plainMessage.setStringProperty("_type", SimpleMessage.class.getName());
                        return plainMessage;
                    } catch (JsonProcessingException e) {
                        throw new JMSException("conversion to json failed: " + e.getMessage());
                    }
                });

        String responseText = responseMessage.getText();
        SimpleMessage responseConverted = objectMapper.readValue(responseText, SimpleMessage.class);
        System.out.println("Driver.sendAndReceive got response: " + responseConverted);
    }
}
