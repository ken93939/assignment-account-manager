package com.acmebank.accountmanager.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acmebank.accountmanager.dto.TransferRequest;
import com.acmebank.accountmanager.entity.Account;
import com.acmebank.accountmanager.entity.Customer;
import com.acmebank.accountmanager.entity.Transaction;
import com.acmebank.accountmanager.repository.AccountRepository;
import com.acmebank.accountmanager.repository.CustomerRepository;
import com.acmebank.accountmanager.repository.TransactionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

	@InjectMocks
	AccountServiceImpl accountService;

	@Mock
	CustomerRepository customerRepository;

	@Mock
	TransactionRepository transactionRepository;

	@Mock
	AccountRepository accountRepository;

	Customer customer;

	Account fromAccount;

	Account toAccount;

	TransferRequest transferRequest;

	@BeforeEach
	void setup() {
		customer = new Customer();
		fromAccount = new Account();
		fromAccount.setBalance(50000);
		fromAccount.setAccountNumber("from");
		customer.setAccounts(List.of(fromAccount));

		toAccount = new Account();
		toAccount.setBalance(100000);
		toAccount.setAccountNumber("to");

		transferRequest = new TransferRequest();
		transferRequest.setToAccount("to");
		transferRequest.setAmount(25000);
		transferRequest.setTransactionID("transId");
	}

	@Test
	void testGetAccountsSuccess() {
		when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
		List<Account> actual = accountService.getAccounts("test");
		assertEquals(1, actual.size());
		assertEquals("from", actual.get(0).getAccountNumber());
	}

	@Test
	void testGetAccountThrowException() {
		when(customerRepository.findById(anyString())).thenReturn(
			Optional.ofNullable(null));
		assertThrows(IllegalArgumentException.class, () -> accountService.getAccounts("test"));
	}

	@Test
	void testTransferSuccess() {
		when(transactionRepository.findById(anyString())).thenReturn(Optional.empty());
		when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
		when(accountRepository.findById(anyString())).thenReturn(Optional.of(toAccount));
		boolean acutal = accountService.transfer("test", "from", transferRequest);
		ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(
			Transaction.class);
		verify(transactionRepository).save(transactionArgumentCaptor.capture());
		assertEquals("from", transactionArgumentCaptor.getValue().getFrom().getAccountNumber());
		assertEquals("to", transactionArgumentCaptor.getValue().getTo().getAccountNumber());
		assertEquals(25000, transactionArgumentCaptor.getValue().getAmount(), 0.0001);
		assertTrue(acutal);
	}

	@Test
	void testTransferThrowExceptionForIncorrectAmount() {
		TransferRequest incorrectAmountRequest = new TransferRequest();
		incorrectAmountRequest.setAmount(0);
		assertThrows(IllegalArgumentException.class,
			() -> accountService.transfer("test", "test", incorrectAmountRequest));
	}

	@Test
	void testTransferThrowExceptionForDuplicateTransaction() {
		transferRequest.setTransactionID("test");
		Transaction transaction = new Transaction();
		transaction.setTransactionId("test");
		when(transactionRepository.findById(transferRequest.getTransactionID())).thenReturn(
			Optional.of(transaction));
		boolean actual = accountService.transfer("test", "test", transferRequest);
		verify(transactionRepository, times(0)).save(any());
		assertTrue(actual);
	}

	@Test
	void testTransferThrowExceptionForTransferAmountGreaterThanBalance() {
		transferRequest.setAmount(100000);
		transferRequest.setTransactionID("123");
		when(transactionRepository.findById(anyString())).thenReturn(Optional.empty());
		when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
		when(accountRepository.findById(anyString())).thenReturn(Optional.of(toAccount));
		assertThrows(IllegalArgumentException.class,
			() -> accountService.transfer("test", "from", transferRequest));
	}

	@Test
	void testTransferThrowExceptionWhenFromAccountNotFound() {
		Customer customer = new Customer();
		Account nonFromAccount = new Account();
		nonFromAccount.setAccountNumber("notFrom");
		nonFromAccount.setBalance(50000);
		customer.setAccounts(List.of(nonFromAccount));
		when(transactionRepository.findById(anyString())).thenReturn(Optional.empty());
		when(customerRepository.findById(anyString())).thenReturn(Optional.ofNullable(null))
			.thenReturn(Optional.of(customer));
		assertThrows(IllegalArgumentException.class,
			() -> accountService.transfer("test", "from", transferRequest));
		assertThrows(IllegalArgumentException.class,
			() -> accountService.transfer("test", "from", transferRequest));
	}

	@Test
	void testTransferThrowExceptionWhenToAccountNotFound() {
		when(transactionRepository.findById(anyString())).thenReturn(Optional.empty());
		when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
		when(accountRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));
		assertThrows(IllegalArgumentException.class,
			() -> accountService.transfer("test", "from", transferRequest));
	}
}