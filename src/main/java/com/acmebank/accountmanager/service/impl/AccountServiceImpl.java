package com.acmebank.accountmanager.service.impl;

import com.acmebank.accountmanager.dto.TransferRequest;
import com.acmebank.accountmanager.entity.Account;
import com.acmebank.accountmanager.entity.Customer;
import com.acmebank.accountmanager.entity.Transaction;
import com.acmebank.accountmanager.repository.AccountRepository;
import com.acmebank.accountmanager.repository.CustomerRepository;
import com.acmebank.accountmanager.repository.TransactionRepository;
import com.acmebank.accountmanager.service.AccountService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

	private CustomerRepository customerRepository;

	private TransactionRepository transactionRepository;

	private AccountRepository accountRepository;

	public AccountServiceImpl(CustomerRepository customerRepository,
		TransactionRepository transactionRepository, AccountRepository accountRepository) {
		this.customerRepository = customerRepository;
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public List<Account> getAccounts(String userId) {
		Optional<Customer> userOptional = customerRepository.findById(userId);
		return userOptional.map(user -> user.getAccounts())
			.orElseThrow(() -> new IllegalArgumentException());
	}

	@Transactional
	public boolean transfer(String userId, String fromAccountStr, TransferRequest transferRequest) {
		if (transferRequest.getAmount() <= 0) {
			throw new IllegalArgumentException();
		}
		Optional<Transaction> transactionOptional = this.transactionRepository.findById(
			transferRequest.getTransactionID());
		if (transactionOptional.isPresent()) {
			return true;
		}
		Optional<Customer> userOptional = this.customerRepository.findById(userId);
		Account fromAccount = userOptional.map(usr -> usr.getAccounts())
			.orElseThrow(() -> new IllegalArgumentException())
			.stream()
			.filter(ac -> ac.getAccountNumber().equals(fromAccountStr))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException());
		Optional<Account> toAccountOptional = accountRepository.findById(
			transferRequest.getToAccount());
		Account toAccount = toAccountOptional.orElseThrow(() -> new IllegalArgumentException());
		fromAccount.transferOut(transferRequest.getAmount());
		toAccount.transferIn(transferRequest.getAmount());
		Transaction transaction = Transaction.createTransaction(transferRequest.getTransactionID(),
			transferRequest.getAmount(), fromAccount, toAccount);
		accountRepository.saveAll(List.of(fromAccount, toAccount));
		transactionRepository.save(transaction);
		return true;
	}
}
