package it.unina.dietiestates25.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class GenericMail {

    private String subject;
    private String body;
    private String to;
}
