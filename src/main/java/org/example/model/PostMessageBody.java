package org.example.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMessageBody {
    @NonNull
    String body;
    String subject;
}
