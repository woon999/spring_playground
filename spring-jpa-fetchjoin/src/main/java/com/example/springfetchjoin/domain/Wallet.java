package com.example.springfetchjoin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Wallet {

	@Id @GeneratedValue
	@Column(name = "wallet_id")
	private Long id;

	private String amount;

	@OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY)
	private Member member;

}
