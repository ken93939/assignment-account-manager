package com.acmebank.accountmanager.service;

import com.acmebank.accountmanager.dto.TransferRequest;
import com.acmebank.accountmanager.entity.Account;
import java.util.List;

public interface AccountService {

	public List<Account> getAccounts(String userId);

	public boolean transfer(String userId, String fromAccount, TransferRequest transferRequest);
}
