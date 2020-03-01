package kr.co.loosie.foody.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MenuItem {

    @Id
    @GeneratedValue
    private long id;

    private long restaurantId;

    private String name;

    public MenuItem(){}

    public MenuItem(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
