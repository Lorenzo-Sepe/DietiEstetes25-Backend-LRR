package it.unina.dietiestates25.entity.utilities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Point {
    private double latitudine;
    private double longitudine;
}
