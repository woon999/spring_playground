package com.study.Jpawebapp.settings;

import com.study.Jpawebapp.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * setting form을 채울 Data (DTO)
 */
@Data
@NoArgsConstructor
public class Profile {

    private String bio;

    private String url;

    private String occupation;

    private String location;

    public Profile(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }
}
