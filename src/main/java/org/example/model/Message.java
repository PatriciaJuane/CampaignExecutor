package org.example.model;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @NonNull
    private Long id;
    @NonNull
    private Long from; // candidateId or userId
    @NonNull
    private Long to; // candidateId or userId

    private String subject;
    @NonNull
    private String body;
    private Date sentAt;
    private Date openedAt;
}
