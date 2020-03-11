package kr.co.loosie.foody.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String email;

    @NotEmpty
    private String name;

    @NotNull
    private Long level;

    private String password;

    public Boolean isAdmin() {
        return level >= 100;
    }

    public Boolean isActive() {
        return level>0;
    }

    public void deactivate() {
        level = 0L;
    }

    @JsonIgnore
    public String getAccessToken() {
        if(password == null)
            return "";
        return password.substring(0,10);
    }
}
