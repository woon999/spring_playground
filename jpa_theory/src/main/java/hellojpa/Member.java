package hellojpa;

import hellojpa.embedded.Address;
import hellojpa.embedded.Period;

import javax.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    // 기간
    @Embedded
    private Period workPeriod;

    // 집 주소
    @Embedded
    private Address homeAddress;

    // 직장 주소
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name ="city",
                        column = @Column(name="WORK_CITY")),
            @AttributeOverride(name ="street",
                    column = @Column(name="WORK_STREET")),
            @AttributeOverride(name ="zipcode",
                    column = @Column(name="WORK_ZIPCODE")),
    })
    private Address workAddress;


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

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }
}
