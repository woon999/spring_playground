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
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                    .setFirstResult(5) // pagination
//                    .setMaxResults(10) // pagination
//                    .getResultList();
//
//            for(Member member : result){
//                System.out.println("member.getName() = " + member.getName());
//            }

            // 비영속
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("loosie");

            //영속
//            System.out.println("=====BEFORE=====");
//            em.persist(member);
//            System.out.println("=====AFTER=====");

            // 1차 캐시 조회
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("loosie");
//            em.persist(member);

//            Member member1 = em.find(Member.class, 101L);
//            System.out.println("member1.getId() = " + member1.getId());;
//            System.out.println("member1.getName() = " + member1.getName());


            //DB에서 조회
//            Member member1 = em.find(Member.class, 101L); //DB
//            Member member2 = em.find(Member.class, 101L); // 1차

            // 영속 엔티티 동일성 보장
//            System.out.println(member1 == member2); // true

            // 트랜잭션을 지원하는 쓰기 지연
//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");
//
//            em.persist(member1);
//            em.persist(member2);
//
//            System.out.println("-------------");

            // 변경감지
//            Member member = em.find(Member.class, 150L);
//            member.setName("ZZZZ");
//
//            System.out.println("-------------");

            // em.flush() 사용
//            Member m ember = new Member(200L, "member200");
//            em.persist(member);
//            em.flush();
//            System.out.println("-------------");

            // 준영속 상태
//            Member member = em.find(Member.class, 150L); // 1차 캐시 저장
//            member.setName("AAAAAA");
//
//            em.clear(); // 영속성 컨텍스트 초기화
//
//            Member member2 = em.find(Member.class, 150L); // 다시 1차 캐시 저장
//            System.out.println("===============");


            // EnumType.STRING vs ORDINAL TEST
            // ORDINAL 사용 x -> Enum 데이터 추가시 기존에 DB에 저장된 타입들 변경 안되어서 혼선 발생
//            Member member = new Member();
//            member.setId(3L);
//            member.setUsername("loosie3");
//            member.setRoleType(RoleType.GUSET);


            // 기본 키 매핑
            Member member = new Member();
            member.setUsername("B");

            em.persist(member);
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

