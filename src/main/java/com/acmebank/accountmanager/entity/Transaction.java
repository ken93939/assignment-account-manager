package com.acmebank.accountmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String transactionId;

	@Column(nullable = false)
	private double amount;

	@OneToOne
	@JoinColumn(name = "from_account", referencedColumnName = "account_number")
	private Account from;

	@OneToOne
	@JoinColumn(name = "to_account", referencedColumnName = "account_number")
	private Account to;

	public static Transaction createTransaction(String transactionId, double amount, Account from,
		Account to
	) {
		return new Transaction(transactionId, amount, from, to);
	}
}
