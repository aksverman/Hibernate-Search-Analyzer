package com.rudra.aks.customer.service;

import java.util.List;

import com.rudra.aks.bo.CustomerBO;

public interface CustomerService {
	int save(CustomerBO customer);
	List<CustomerBO>	search(String columnName, String searchText);
	void delete(CustomerBO customer);
}
