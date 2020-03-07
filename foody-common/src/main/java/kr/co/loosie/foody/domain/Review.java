package kr.co.loosie.foody.domain;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Review {

    @Id
    @GeneratedValue
    private long id;

    @Setter
    private long restaurantId;


    private String name;

    @NotNull
    private Integer score;

    @NotEmpty
    private String description;
}
