package com.mb.zad4_2;

import com.mb.zad4_2.models.BolidState;
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
public class Monitor {
    private final JmsTemplate jmsTemplate;

    public void checkBolidState(BolidState state) {
        long temperature = state.getTemperature();
        long tireWear = state.getTireWear();
        long brakeWear = state.getBrakeWear();
        long fuel = state.getFuel();

        boolean criticalWarning = temperature > 120 || tireWear < 10 || brakeWear < 10 || fuel < 10;
        boolean softWarning = !criticalWarning && (temperature > 110 || tireWear < 20 || brakeWear < 20 || fuel < 20);

        if(criticalWarning || softWarning) {
            jmsTemplate.convertAndSend(JmsConfig.TOPIC_WARNING, state, message -> {
                String type = "";

                if(criticalWarning) {
                    type = "critical";
                }

                if(softWarning) {
                    type = "soft";
                }

                message.setJMSType(type);
                return message;
            });

            System.out.println("Monitor.checkBolidState - sent warning: "+state);
        }

    }

    @JmsListener(destination = JmsConfig.TOPIC_BOLID, containerFactory = "topicConnectionFactory")
    public void receiveBolidState(@Payload BolidState convertedState, @Headers MessageHeaders messageHeaders, Message message) {
        System.out.println("Monitor.receiveBolidState, state: "+convertedState);

        checkBolidState(convertedState);
    }
}
