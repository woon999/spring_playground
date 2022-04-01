package com.example.springfetchjoin.domain;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Member {

	@Id @GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String name;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name ="team_id")
	private Team team;

	@OneToOne( fetch = LAZY)
	@JoinColumn(name = "wallet_id")
	private Wallet wallet;

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
		wallet.setMember(this);
	}
}
