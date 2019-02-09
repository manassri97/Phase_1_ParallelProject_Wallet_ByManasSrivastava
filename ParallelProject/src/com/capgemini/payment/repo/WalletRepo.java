package com.capgemini.payment.repo;

import java.util.List;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.bean.Transaction;

public interface WalletRepo {
	public boolean save(Customer customer);
	public Customer findOne(String mobileNo);
	public boolean saveTransaction(Transaction transcaction, Customer customer);
	public List<Transaction> retrieveTransaction(String tmobileNo);
}
