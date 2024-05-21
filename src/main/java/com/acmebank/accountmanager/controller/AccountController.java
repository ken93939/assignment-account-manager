package com.acmebank.accountmanager.controller;

import com.acmebank.accountmanager.dto.AccountDTO;
import com.acmebank.accountmanager.dto.TransferRequest;
import com.acmebank.accountmanager.entity.Account;
import com.acmebank.accountmanager.model.CustomUserDetails;
import com.acmebank.accountmanager.service.AccountService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

	private AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping("/accounts")
	public ResponseEntity<List<AccountDTO>> getAccounts(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		String userId = customUserDetails.getCustomer().getId();
		List<Account> accountList = accountService.getAccounts(userId);
		List<AccountDTO> accountDTOList = accountList.stream().map(ac -> toAccountDTO(ac))
			.collect(Collectors.toList());
		return new ResponseEntity<>(accountDTOList, HttpStatus.OK);
	}

	@PostMapping("/account/{accountNumber}/transfer")
	public ResponseEntity<Boolean> transfer(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable(name = "accountNumber") String fromAccount,
		@RequestBody TransferRequest request) {
		String userId = customUserDetails.getCustomer().getId();
		boolean result = this.accountService.transfer(userId, fromAccount, request);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	private AccountDTO toAccountDTO(Account account) {
		AccountDTO dto = new AccountDTO();
		dto.setAccountNumber(account.getAccountNumber());
		dto.setBalance(account.getBalance());
		return dto;
	}
}
