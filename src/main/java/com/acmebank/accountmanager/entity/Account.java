package com.acmebank.accountmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "account")
public class Account {

	@Id
	@Column(name = "account_number")
	private String accountNumber;

	@Column()
	private double balance;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer owner;

	public void transferOut(double amount) {
		if (amount > balance) {
			throw new IllegalArgumentException("Cannot transfer more than balance");
		}
		this.balance -= amount;
	}

	public void transferIn(double amount) {
		this.balance += amount;
	}
}
