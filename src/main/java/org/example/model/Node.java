package org.example.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Node {

    @NonNull
    private Long id;
    private CampaignType type;
    private ChannelType channel;
    private String message;
    private String subjectLine;

    private Long delaySeconds;
}
