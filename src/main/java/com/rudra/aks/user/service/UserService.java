package com.rudra.aks.user.service;

import java.util.List;

import com.rudra.aks.bo.UserBO;

public interface UserService {

	int saveUser(UserBO userBO);

	List<UserBO> search(String columnName, String searchText);

}
