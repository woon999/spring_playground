package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    // EnumType.ORDINAL이 default -> 컬럼이 1,2,3,4 숫자로 들어감
    //   -> 문제 중간에 어떤 다른 상태역이 생기면 순서가 밀리게되면서 망함
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY , COMP

}
