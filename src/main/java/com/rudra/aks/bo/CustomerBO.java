package com.rudra.aks.bo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

@Entity
@Table(name = "Customer")
@Indexed
@AnalyzerDef(name = "customanalyzer",
tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
filters = {
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
	  @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
	    @Parameter(name = "language", value = "English")
	  })
})
public class CustomerBO {
	

	private int		customerid;
	
	@Field(store = Store.YES)
	//@Analyzer(definition= "customanalyzer")
	private String  customername;
	
	@Field(store = Store.YES)
	private String  mobileno;
	private String  emailid;
	
	@Id
	@GeneratedValue
	public int getCustomerid() {
		return customerid;
	}
	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getEmailid() {
		return emailid;
	}
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	
	@Override
	public String toString() {
		return "Customer [customerid=" + customerid + ", customername=" + customername + ", mobileno=" + mobileno
				+ ", emailid=" + emailid + "]";
	}
}
