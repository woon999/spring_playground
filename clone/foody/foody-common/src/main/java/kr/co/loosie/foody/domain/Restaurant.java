package kr.co.loosie.foody.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private Long categoryId;

    @NotEmpty
    private String name;
    @NotEmpty
    private String address;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenuItem> menuItems;


    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Review> reviews;


    public String getInfo() {
        return name + " in " + address;
    }


    public void updateInformation(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void setMenuItem(List<MenuItem> menuItems) {
        this.menuItems = new ArrayList<>(menuItems);
//        for (MenuItem menuItem : menuItems) {
//            addMenuItem(menuItem);
//        }
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = new ArrayList<>(reviews);
    }

    //    setMenuItems에 직접 arraylist를 불러와 넣어주므로
//    addMenuItem메서드는 불필요
//    public void addMenuItem(MenuItem menuItem) {
//
//        menuItems.add(menuItem);
//    }
}
