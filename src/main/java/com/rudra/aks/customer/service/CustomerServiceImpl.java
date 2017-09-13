package com.rudra.aks.customer.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rudra.aks.bo.CustomerBO;
import com.rudra.aks.customer.dao.CustomerDAO;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerDAO		customerdao;
	
	private static final Logger logger = Logger.getLogger(CustomerServiceImpl.class);
	
	
	public int save(CustomerBO customer) {
		return customerdao.save(customer);
	}


	public List<CustomerBO> search(String columnName, String searchText) {
		return customerdao.search(columnName, searchText);
	}


	@Override
	public void delete(CustomerBO customer) {
		customerdao.delete(customer);
		
	}
	
	
}
