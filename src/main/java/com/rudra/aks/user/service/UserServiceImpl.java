package com.rudra.aks.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rudra.aks.bo.UserBO;
import com.rudra.aks.user.dao.UserDAO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO		userDAO;
	
	@Override
	public int saveUser(UserBO userBO) {
		return userDAO.saveUser(userBO);
	}

	@Override
	public List<UserBO> search(String columnName, String searchText) {
		return userDAO.search(columnName, searchText);
	}

}
