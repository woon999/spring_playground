package hellojpa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hellojpa.domain.Member;

public class JpaMain2 {
    // META-INF -> persistence-unit name :hello
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();
    // 트랜잭션 시작
    EntityTransaction tx = em.getTransaction();

    @BeforeEach
    void setUp(){
        tx.begin();
    }

    @AfterEach
    void end(){
        try{
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    @Test
    void insert(){
        Member member = new Member();
        member.setName("hello wold");
        em.persist(member);
    }

    @Test
    void find(){
        Member findMember = em.find(Member.class, 1L);
        System.out.println("findMember = " +findMember);
    }

    @Test
    void delete(){
        Long memberId = 2L;
        Member findMember = em.find(Member.class, memberId);
        if(findMember != null) {
            System.out.println("member delete = " + memberId);
            em.remove(findMember);
            Member result = em.find(Member.class, memberId);
            assertEquals(result, null);
        }
    }

    // 더티 체킹 - 자동으로 업데이트
    @Test
    void update(){
        Member findMember = em.find(Member.class, 1L);
        findMember.setName("hoelll???");
    }

    /**
     * jpa -> 엔티티 중심 개발. 검색쿼리도 엔티티 객체를 대상으로 검색
     * 한계: DB 데이터를 모두 객체로 변환해서 검색하는 건 불가능
     *
     * 그래서! JPQL (Java Persistence Query Language) 사용
     * JPQL은 엔티티 객체를 대상으로 처리 <-> SQL은 DB 테이블을 대상으로 처리
     *
     * JQPL
     *  - 가장 단순한 조회 방법
     *      - EntityManager.find()
     *      - 객체 그래프 탐색(a.getB().getC())
     *  - 나이가 18살 이상인 회원을 모두 검색하고 싶으면 em.createQuery()로 객체를 대상으로 쿼리 처리
     */
    @Test
    void jpql(){
        List<Member> result = em.createQuery("select m from Member as m", Member.class)
            .getResultList();

        for (Member m : result) {
            System.out.println("member = " + m.getName());
        }
    }

    @Test
    void persistence(){
        // 비영속
        Member member = new Member();
        member.setName("hello world");
        System.out.println("====== BEFORE =======");

        // 영속
        em.persist(member);
        System.out.println("====== AFTER =======");
    }

    @Test
    void flush(){
        Member member = new Member();
        member.setName("hello world");
        em.persist(member);
        System.out.println("====== BEFORE =======");
        em.flush(); // flush 처리
        System.out.println("====== AFTER =======");
    }

    /**
     * 준영속 상태 - 영속성 분리
     *
     * em.detach(entity): 특정 엔티티만 준영속 상태로 전환
     * em.clear(): 영속성 컨텍스트를 완전히 초기화
     * em.close(): 영속성 컨텍스트 종료
     */
    @Test
    void detach(){
        Member member = new Member();
        member.setName("hello world!!!");

        System.out.println("====== BEFORE =======");
        em.detach(member);
        System.out.println("====== AFTER =======");
    }

}

