package org.example.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    Long id;
    ChannelType type;
    ChannelStatus status;
}
