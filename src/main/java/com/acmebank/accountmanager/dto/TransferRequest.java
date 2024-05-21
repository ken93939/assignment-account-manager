package com.acmebank.accountmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {

	@NotNull
	String transactionID;

	@NotNull
	String toAccount;

	@Min(0)
	double amount;
}
