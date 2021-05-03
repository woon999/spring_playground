package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // META-INF -> persistence-unit name :hello
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        // 트랜잭션 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();


        try{
//            1.멤버 저장
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("helloB");
//            --em save--
//            em.persist(member);

//            2.멤버 수정
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("helloJPA");

//            jpql 멤버 조회
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5) // pagination
                    .setMaxResults(10) // pagination
                    .getResultList();
            
            for(Member member : result){
                System.out.println("member.getName() = " + member.getName());
            }

            // 트랜잭션 커밋
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}

