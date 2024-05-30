package org.example.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    @NonNull
    Long id;
    @NonNull
    String firstName;
    @NonNull
    String lastName;
    String email;
    String linkedin;
    Resume cv;
}
