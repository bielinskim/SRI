package com.mb.zad4_2.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleMessage {
    private static long idIndex = 0;
    public static long
    nextId() { return
            idIndex++;
    }
    private long id;
    private String message;
}
