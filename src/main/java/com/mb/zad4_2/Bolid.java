package com.mb.zad4_2;

import com.mb.zad4_2.models.BolidState;
import org.springframework.jms.core.JmsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Bolid {
    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 15000)
    public void sendState() {
        BolidState state = BolidState.builder()
                .id(BolidState.nextId())
                .createdAt(LocalDateTime.now())
                .speed(200)
                .temperature(90)
                .tireWear(45)
                .tireWear(60)
                .fuel(9)
                .build();
        jmsTemplate.convertAndSend(JmsConfig.TOPIC_BOLID, state);
        System.out.println("Bolid.sendState - sent state: "+state);
    }
}
