package com.capgemini.payment.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.bean.Wallet;
import com.capgemini.payment.exceptions.InsufficientWalletBalanceException;
import com.capgemini.payment.exceptions.PhoneNumberAlreadyExist;
import com.capgemini.payment.exceptions.TransactionFailedException;
import com.capgemini.payment.exceptions.WalletAccountDoesNotExist;
import com.capgemini.payment.repo.WalletRepo;
import com.capgemini.payment.repo.WalletRepoImpl;
import com.capgemini.payment.service.WalletService;
import com.capgemini.payment.service.WalletServiceImpl;

public class TestCases {
	WalletRepo walletRepo = new WalletRepoImpl();
	WalletService walletService = new WalletServiceImpl(walletRepo);
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	/*
	 * case : Wallet Creation
	 * 
	 */
	@Test
	public void whenWalletAccountIsCreatedSuccessfully() throws PhoneNumberAlreadyExist { 
		Customer customer = new Customer();
		Wallet wallet = new Wallet();
		customer.setName("manas");
		customer.setMobileno("9795716325");
		wallet.setBalance(new BigDecimal(500));
		customer.setWallet(wallet);
		customer.setList(new ArrayList<>());
		assertEquals(customer, walletService.createAccount("manas", "9795716325", new BigDecimal(500)));
	}
	
	@Test(expected=com.capgemini.payment.exceptions.PhoneNumberAlreadyExist.class)
	public void whenWalletMoblileNumberAlreadyExist() throws PhoneNumberAlreadyExist { 
		
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
	}
	
	/*
	 * case : Withdraw From Wallet
	 * 
	 */
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenWalletAccountDoesNotExistWhileWithdrawing() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.withdrawAmount("9795716335",new BigDecimal(500));
	}
	
	@Test(expected=com.capgemini.payment.exceptions.InsufficientWalletBalanceException.class)
	public void whenWalletHaveInsufficientBalanceWhileWithdrawing() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.withdrawAmount("9795716325",new BigDecimal(600));
	}
	
	@Test
	public void whenWithdrawFromWalletSuccessfullWhileWithdrawing() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.withdrawAmount("9795716325",new BigDecimal(400));
	}
	
	/*
	 * case : Deposit In Wallet
	 * 
	 */
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenWalletAccountDoesNotExistWhileDepositing() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.depositAmount("9795716335",new BigDecimal(500));
	}
	
	@Test
	public void whenDepositFromWalletSuccessfull() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.depositAmount("9795716325",new BigDecimal(400));
	}
	
	/*
	 * case : Show Wallet Balance
	 * 
	 */
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenShowWalletBalanceIsCalledThenWalletDoesNotExist() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.showBalance("9795716335");
	}
	
	@Test
	public void whenShowWalletBalanceIs() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
	
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.showBalance("9795716325");
	}
	
	/*
	 * case : Fund Transfer
	 * 
	 */
	
	@Test(expected=com.capgemini.payment.exceptions.InsufficientWalletBalanceException.class)
	public void whenSourceWalletBalanceIsInsufficient() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.createAccount("baba", "9120760410", new BigDecimal(500));
		walletService.withdrawAmount("9795716325",new BigDecimal(600));
		walletService.depositAmount("9120760410", new BigDecimal(600));
	}
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenSourceWalletAccountDoesNotExist() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.createAccount("baba", "9120760410", new BigDecimal(500));
		walletService.withdrawAmount("9795716335",new BigDecimal(400));
		walletService.depositAmount("9120760410", new BigDecimal(400));
	}
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenTargetWalletAccountDoesNotExist() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.createAccount("baba", "9120760410", new BigDecimal(500));
		walletService.withdrawAmount("9795716325",new BigDecimal(400));
		walletService.depositAmount("9120760412", new BigDecimal(400));
	}
	
	@Test
	public void whenFundTransferIsSuccessfull() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist { 
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.createAccount("baba", "9120760410", new BigDecimal(500));
		walletService.withdrawAmount("9795716325",new BigDecimal(400));
		walletService.depositAmount("9120760410", new BigDecimal(400));
	}
	
	
}
