package com.capgemini.payment.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.bean.Transaction;
import com.capgemini.payment.bean.Wallet;
import com.capgemini.payment.exceptions.InsufficientWalletBalanceException;
import com.capgemini.payment.exceptions.NoTransactionDoneException;
import com.capgemini.payment.exceptions.PhoneNumberAlreadyExist;
import com.capgemini.payment.exceptions.TransactionFailedException;
import com.capgemini.payment.exceptions.WalletAccountDoesNotExist;
import com.capgemini.payment.repo.WalletRepo;

public class WalletServiceImpl implements WalletService {

	String id = "IGAFREF02145302";
	int counter=1;
	WalletRepo walletRepo;
	public WalletServiceImpl(WalletRepo walletRepo) {
		this.walletRepo=walletRepo;
	}

	@Override
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) throws PhoneNumberAlreadyExist {
		List<Transaction> list = new ArrayList<>();
		Customer customer = new Customer();
		Wallet wallet = new Wallet();
		customer.setName(name);
		customer.setMobileno(mobileNo);
		wallet.setBalance(amount);
		customer.setWallet(wallet);
		customer.setList(list);
		if(walletRepo.save(customer))
		{
			return customer;
		}
		else
		{
			throw new PhoneNumberAlreadyExist("Wallet already exist \nTry with another mobile number");
		}
	}

	@Override
	public Customer showBalance(String mobileNo) throws WalletAccountDoesNotExist {
		Customer customer = new Customer();
		
		customer = walletRepo.findOne(mobileNo);
		if(customer!=null)
			return walletRepo.findOne(mobileNo);
		else
			throw new WalletAccountDoesNotExist("No Such Wallet Exists");
	}

	@Override
	public List<Customer> fundTransfer(String sourceMobile, String targetMobileNo, BigDecimal amount) throws InsufficientWalletBalanceException, WalletAccountDoesNotExist, TransactionFailedException {
		Customer customer = new Customer();
		Customer customer1 = new Customer();
		List<Customer> list =new ArrayList<>();
		customer = walletRepo.findOne(sourceMobile);
		if(customer!=null)
		{
			list.add(withdrawAmount(sourceMobile, amount));
		}
		else
		{
			throw new WalletAccountDoesNotExist("Source Mobile Not Found");
		}
		customer1 = walletRepo.findOne(targetMobileNo);
		if(customer1!=null)
		{
			list.add(depositAmount(targetMobileNo, amount));
			return list;
		}
		else
		{
			throw new WalletAccountDoesNotExist("Target Mobile Not Found");
		}
		
	}

	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) throws WalletAccountDoesNotExist, TransactionFailedException {
		Customer customer = new Customer();
		Transaction transaction =new Transaction();
		
		customer = walletRepo.findOne(mobileNo);
		if(customer!=null)
		{
			customer.getWallet().setBalance(customer.getWallet().getBalance().add(amount));
			transaction = createTransaction("deposit",amount,mobileNo,customer);
			if(walletRepo.saveTransaction(transaction, customer))
			{
				return customer;
			}
			else
			{
				throw new TransactionFailedException("transaction aborted");
			}
		}
		throw new WalletAccountDoesNotExist("Target Mobile Number Not Found");
	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) throws InsufficientWalletBalanceException, WalletAccountDoesNotExist, TransactionFailedException {
		Customer customer = new Customer();
		Transaction transaction =new Transaction();
		
		customer = walletRepo.findOne(mobileNo);
		if(customer!=null)
		{
			int i=customer.getWallet().getBalance().compareTo(amount);
			if(i<0)
			{
				throw new InsufficientWalletBalanceException("Not Enough Balance");
			}
			else
			{
				customer.getWallet().setBalance(customer.getWallet().getBalance().subtract(amount));
				transaction = createTransaction("withdraw",amount,mobileNo,customer);
				if(walletRepo.saveTransaction(transaction, customer))
				{
					return customer;
				}
				else
				{
					throw new TransactionFailedException("transaction aborted");
				}
			}
 		}
		else
			throw new WalletAccountDoesNotExist("No Wallet Exist");
	}
	
	@Override
	public List<Transaction> findTransaction(String tmobileNo) throws NoTransactionDoneException {
		List<Transaction> list =new ArrayList<>();
		list = walletRepo.retrieveTransaction(tmobileNo);
		if(list.size()!=0)
		{
			return list;
		}
		else
			throw new NoTransactionDoneException("You have not done any transactions");
	}

	private Transaction createTransaction(String string, BigDecimal amount, String mobileNo, Customer customer) {
		String id1="";
		Transaction transaction =new Transaction();
		switch(string)
		{
			case "withdraw":id1=id+Integer.toString(counter);
							transaction.setId(id1);
							transaction.setTmobileNo(mobileNo);
							transaction.setDebit(amount);
							transaction.setTotal(customer.getWallet().getBalance());
							counter++;
							break;
			case "deposit":id1=id+Integer.toString(counter);
							transaction.setId(id1);
							transaction.setTmobileNo(mobileNo);
							transaction.setCredit(amount);
							transaction.setTotal(customer.getWallet().getBalance());
							counter++;
							break;
		}
		return transaction;
	}

	
}