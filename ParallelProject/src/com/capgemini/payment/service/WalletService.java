package com.capgemini.payment.service;

import java.math.BigDecimal;
import java.util.List;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.bean.Transaction;
import com.capgemini.payment.exceptions.InsufficientWalletBalanceException;
import com.capgemini.payment.exceptions.NoTransactionDoneException;
import com.capgemini.payment.exceptions.PhoneNumberAlreadyExist;
import com.capgemini.payment.exceptions.TransactionFailedException;
import com.capgemini.payment.exceptions.WalletAccountDoesNotExist;

public interface WalletService {
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) throws PhoneNumberAlreadyExist;
	public Customer showBalance(String mobileNo) throws WalletAccountDoesNotExist;
	public List<Customer> fundTransfer(String sourceMobile, String targetMobileNo, BigDecimal amount) throws InsufficientWalletBalanceException, WalletAccountDoesNotExist, TransactionFailedException;
	public Customer depositAmount(String mobileNo, BigDecimal amount) throws WalletAccountDoesNotExist, TransactionFailedException;
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) throws InsufficientWalletBalanceException, WalletAccountDoesNotExist, TransactionFailedException;
	public List<Transaction> findTransaction(String tmobileNo) throws NoTransactionDoneException;
}
