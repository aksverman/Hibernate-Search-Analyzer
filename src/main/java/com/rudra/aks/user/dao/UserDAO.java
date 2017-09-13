package com.rudra.aks.user.dao;

import java.util.List;

import com.rudra.aks.bo.UserBO;

public interface UserDAO {

	int saveUser(UserBO userBO);

	List<UserBO> search(String columnName, String searchText);

}
