package com.rudra.aks.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rudra.aks.bo.CustomerBO;
import com.rudra.aks.customer.service.CustomerService;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

	@Autowired
	CustomerService		customerService;
	
	private static final Logger logger = Logger.getLogger(CustomerController.class);
	
	@RequestMapping(path = "/save", method = RequestMethod.POST)
	public	ResponseEntity<?>	saveCustomer(@RequestBody CustomerBO customer) {
		logger.info("customer controller >>");
		try {
			return new ResponseEntity<Integer>(customerService.save(customer), HttpStatus.OK);
		} catch (Exception e ) {
			logger.error("Exception : " + e);
			return new ResponseEntity<String>("Exception : ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(path = "/search/{columnName}/{searchText}", method = RequestMethod.GET)
	public List<CustomerBO>	search(@PathVariable String columnName, @PathVariable String searchText) {
		return customerService.search(columnName, searchText);
	}
	
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	public	ResponseEntity<?>	delete(@RequestBody CustomerBO customer) {
		logger.info("customer controller >>");
		try {
			customerService.delete(customer);
			return new ResponseEntity<>("Customer info deleted...", HttpStatus.OK);
		} catch (Exception e ) {
			logger.error("Exception : " + e);
			return new ResponseEntity<String>("Exception while deletion: ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String	testRestCall() {
		return "Customer Service Starting ....";
	}
}
