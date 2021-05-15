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
//            Member member = new Member();
//            member.setUsername("B");


            //객체를 테이블에 맞추어 모델링
//            //팀 저장
//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            //회원 저장
//            Member member = new Member();
//            member.setName("member1");
//            member.setTeamId(team.getId());
//            em.persist(member);
//
//            //조회
//            Member findMember = em.find(Member.class, member.getId());
//
//            //연관관계가 없음
//            Team findTeam = em.find(Team.class, findMember.getTeamId());


//            //단방향 연관관계
//            //팀 저장
//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            //회원 저장
//            Member member = new Member();
//            member.setName("member1");
//            member.setTeam(team); //단방향 연관관계 설정, 참조 저장
//            em.persist(member);
//
//            //조회
//            Member findMember = em.find(Member.class, member.getId());
//
//            //참조를 사용해서 연관관계 조회
//            Team findTeam = findMember.getTeam();
//            System.out.println("findTeam.getName() = " + findTeam.getName());
//            // 새로운 팀B
//            Team teamB = new Team();
//            teamB.setName("TeamB");
//            em.persist(teamB);
//
//            // 회원1에 새로운 팀B 설정
//            member.setTeam(teamB);
//            //참조를 사용해서 연관관계 조회
//            Team findTeam2 = findMember.getTeam();
//            System.out.println("findTeam2.getName() = " + findTeam2.getName());
//            em.persist(member);


            // 양방향 연관관계
//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            //회원 저장
//            Member member = new Member();
//            member.setName("member1");
//            member.setTeam(team);
//            em.persist(member);
//
//            em.flush();
//            em.clear();
//
//            //조회
//            Member findMember = em.find(Member.class, member.getId());
//            // 역방향 조회
//            List<Member> members =findMember.getTeam().getMembers();
//
//            for(Member m : members){
//                System.out.println("m = " + m.getName());
//            }

            // 양방향 연관관계 주의할 점
            //1. 연관관계의 주인에 값 입력 x
            //2. 양방향 매핑시 연관관계의 주인에 값을 입력해야 한다.
//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setName("member1");
//            member.setTeam(team); // * Member 방향 연관관계 편의 메소드 생성 *
//            team.addMember(member); // * Team 방향 연관관계 편의 메소드 생성 *

            //역방향(주인이 아닌 방향)만 연관관계 설정
//            team.getMembers().add(member);

            //연관관계의 주인에 값 설정
//            member.getTeam(team);

            // toString무한루프 
//            Team findTeam = em.find(Team.class, team.getId());
//            System.out.println("findTeam = " + findTeam);

//            em.persist(member);


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

