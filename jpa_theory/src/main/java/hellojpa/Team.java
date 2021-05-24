package hellojpa;

import hellojpa.maapedsuper.BaseEntity;

import javax.persistence.*;

@Entity
public class Team extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

//    @OneToMany
//    @JoinColumn(name ="TEAM_ID")
//    List<Member> members = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
