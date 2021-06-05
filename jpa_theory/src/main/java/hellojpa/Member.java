package hellojpa;

import hellojpa.embedded.Address;
import hellojpa.embedded.Period;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NamedQuery(
        name = "Member.findByUsername",
        query= "select m from Member m where m.username = :username"
)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username")
    private String username;

    private int age;

    // 기간
//    @Embedded
//    private Period workPeriod;

    // 집 주소
//    @Embedded
//    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="team_id")
    private Team team;

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
// 직장 주소
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name ="city",
//                        column = @Column(name="WORK_CITY")),
//            @AttributeOverride(name ="street",
//                    column = @Column(name="WORK_STREET")),
//            @AttributeOverride(name ="zipcode",
//                    column = @Column(name="WORK_ZIPCODE")),
//    })
//    private Address workAddress;

//    @ElementCollection
//    @CollectionTable(name ="FAVORITE_FOOD", joinColumns =
//            @JoinColumn(name="MEMBER_ID"))
//    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection
//    @CollectionTable(name ="ADDRESS", joinColumns =
//    @JoinColumn(name="MEMBER_ID"))
//    private List<Address> addressHistory = new ArrayList<>();

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "MEMBER_ID")
//    private List<AddressEntity> addressHistory = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }


    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + username + '\'' +
                ", age=" + age + '}';
    }
}
