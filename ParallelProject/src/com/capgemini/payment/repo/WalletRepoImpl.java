package com.capgemini.payment.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.bean.Transaction;

public class WalletRepoImpl implements WalletRepo {

	Map<String, Customer> hashMap = new HashMap<>();
	@Override
	public boolean save(Customer customer) {
		if(hashMap.containsKey(customer.getMobileno()))
			return false;
		else
		{
			hashMap.put(customer.getMobileno(), customer);
			return true;
		}
	}

	@Override
	public Customer findOne(String mobileNo) {
		if(hashMap.containsKey(mobileNo))
		{
			return hashMap.get(mobileNo);
		}
		else
			return null;
	}
	
	@Override
	public boolean saveTransaction(Transaction transaction, Customer customer) {
		List<Transaction> list = customer.getList();
		list.add(transaction);
		customer.setList(list);
		hashMap.replace(customer.getMobileno(), customer);
		return true;
	}

	@Override
	public List<Transaction> retrieveTransaction(String tmobileNo) {
		Customer customer =new Customer();
		customer = hashMap.get(tmobileNo);
		List<Transaction> list1 = customer.getList();
		if(list1.size()==0)
			return list1;
		Collections.reverse(list1);
		if(list1.size()<10)
		{
			return list1;
		}
		else
		{
			List<Transaction> list2 = new ArrayList<>();
			for(int i=0;i<10;i++)
			{
				list2.add(customer.getList().get(i));
			}
			Collections.reverse(list2);
			return list2;
		}
	}
}
