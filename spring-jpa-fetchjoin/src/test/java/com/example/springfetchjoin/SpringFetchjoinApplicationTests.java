package com.example.springfetchjoin;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.springfetchjoin.domain.Member;
import com.example.springfetchjoin.domain.Team;
import com.example.springfetchjoin.domain.Wallet;

@SpringBootTest
@Transactional
class SpringFetchjoinApplicationTests {

	@Autowired
	EntityManager em;

	private Long mId;
	private Long wId;
	private Long tId;
	@BeforeEach
	void setUp(){

		// Member - Team (N:1)
		Team team = new Team();
		team.setName("teamA");
		em.persist(team);
		tId = team.getId();

		Team team2 = new Team();
		team2.setName("teamB");
		em.persist(team2);

		for(int i=1; i<=5; i++){
			Wallet wallet = new Wallet();
			wallet.setAmount("100");

			Member member = new Member();
			member.setName("member"+i);
			if(i%2==0) {
				member.setTeam(team);
			}else{
				member.setTeam(team2);
			}

			member.setWallet(wallet);
			em.persist(member);
			em.persist(wallet);

			wId = wallet.getId();
			mId = member.getId();
		}
		em.flush();
		em.clear();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void manyToOne(){
		List<Member> result = em.createQuery("select m from Member m "
			+ "join fetch m.team t", Member.class)
			.getResultList();

		for (Member member : result) {
			System.out.println("member = " + member.getName() +" - team :" + member.getTeam().getName());
		}
	}

	@Test
	void oneToMany(){
		List<Team> result = em.createQuery("select t from Team t"
			+ " join fetch t.members m", Team.class)
			.getResultList();

		for (Team team1 : result) {
			System.out.println("team1 = " + team1.getName());
			List<Member> members = team1.getMembers();
			for (Member member : members) {
				System.out.println("member = " + member.getName());
			}
		}
	}

	// ManyToOne 주인에게 별칭 - OK
	@Test
	void manyToOne_with_parent_alias(){
		List<Member> result = em.createQuery("select m from Member m "
			+ "join fetch m.team t where m.name='member1'", Member.class)
			.getResultList();

		for (Member member : result) {
			System.out.println("member = " + member.getName() +" - team :" + member.getTeam().getName());
		}
	}

	// ManyToOne 대상에게 별칭 - OK
	@Test
	void manyToOne_with_sub_alias(){
		List<Member> result = em.createQuery("select m from Member m "
			+ "join fetch m.team t where t.name='teamA'", Member.class)
			.getResultList();

		for (Member member : result) {
			System.out.println("member = " + member.getName() +" - team :" + member.getTeam().getName());
		}
	}

	// ManyToOne left join으로 대상에게 별칭 - 일관성 깨짐
	@Test
	void manyToOne_withLeftJoin_sub_alias(){
		List<Member> result = em.createQuery("select m from Member m "
			+ "left join fetch m.team t where t.name='teamA'", Member.class)
			.getResultList();

		for (Member member : result) {
			System.out.println("member = " + member.getName() +" - team :" + member.getTeam().getName());
		}
	}

	// OneToMany 주인에게 별칭 - OK
	@Test
	void oneToMany_with_parent_alias(){
		List<Team> result = em.createQuery("select t from Team t "
			+ "join fetch t.members m where t.name='teamA'", Team.class)
			.getResultList();

		for (Team team1 : result) {
			System.out.println("team1 = " + team1.getName());
			List<Member> members = team1.getMembers();
			for (Member member : members) {
				System.out.println("member = " + member.getName());
			}
		}
	}

	// OneToMany 대상에게 별칭 - 일관성 깨짐
	@Test
	void oneToMany_with_alias(){
		List<Team> result = em.createQuery("select t from Team t"
			+ " join fetch t.members m where m.name='member1'", Team.class)
			.getResultList();

		for (Team team1 : result) {
			System.out.println("team1 = " + team1.getName());
			List<Member> members = team1.getMembers();
			for (Member member : members) {
				System.out.println("member = " + member.getName());
			}
		}
	}

	@Test
	void oneToOne_주인_객체가_조회(){
		Member member = em.createQuery("select m from Member m "
			+ "where m.id = :id"
			, Member.class)
			.setParameter("id", mId)
			.getSingleResult();

		System.out.println(member.getWallet().getClass()); // 프록시 객체
	}

	// Not Lazy
	@Test
	void oneToOne_주인이_아닌_객체가_조회(){
		Wallet wallet = em.createQuery("select w from Wallet w "+
			"where w.id = :id"
			, Wallet.class)
			.setParameter("id" , wId)
			.getSingleResult();

		System.out.println(wallet.getMember().getClass()); // 프록시 객체 x, 진짜 객체
	}

}
