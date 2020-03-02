package kr.co.loosie.foody.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String address;

    @Transient
    private List<MenuItem> menuItems;


    public String getInfo() {
        return name + " in " + address;
    }


    public void setMenuItem(List<MenuItem> menuItems) {
        this.menuItems = new ArrayList<>(menuItems);
//        for (MenuItem menuItem : menuItems) {
//            addMenuItem(menuItem);
//        }
    }

    public void updateInformation(String name, String address) {
        this.name = name;
        this.address = address;
    }

    //    setMenuItems에 직접 arraylist를 불러와 넣어주므로
//    addMenuItem메서드는 불필요
//    public void addMenuItem(MenuItem menuItem) {
//
//        menuItems.add(menuItem);
//    }
}
