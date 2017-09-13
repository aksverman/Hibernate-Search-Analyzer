package com.rudra.aks.customer.dao;

import java.util.List;

import com.rudra.aks.bo.CustomerBO;

public interface CustomerDAO {
	int save(CustomerBO customer);
	List<CustomerBO>	search(String columnName, String searchText);
	void delete(CustomerBO customer);
}
