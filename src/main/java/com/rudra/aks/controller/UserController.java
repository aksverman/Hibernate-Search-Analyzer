package com.rudra.aks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rudra.aks.bo.CustomerBO;
import com.rudra.aks.bo.UserBO;
import com.rudra.aks.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService		userService;
	
	
	@RequestMapping(path = "/save", method = RequestMethod.POST)
	public ResponseEntity<?>	saveUser(@RequestBody UserBO userBO) {
		return new ResponseEntity<>(userService.saveUser(userBO), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/search/{columnName}/{searchText}", method = RequestMethod.GET)
	public List<UserBO>	search(@PathVariable String columnName, @PathVariable String searchText) {
		return userService.search(columnName, searchText);
	}
	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String	testRestCall() {
		return "User Service Starting ....";
	}
}
