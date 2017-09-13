package com.rudra.aks.bo

import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed

import com.fasterxml.jackson.annotation.JsonAutoDetect

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.Sortable

@Sortable(includes=['userName','emailID'])
@CompileStatic
@Canonical
@JsonAutoDetect
@Entity
@Table(name = "SEC_USER_TEST")
//@Indexed
class UserBO {
	
	@Id
	@GeneratedValue
	int userID
	int groupID
	
	@Field
	String userName
	
	@Field
	String firstName
	String middleName
	String lastName
	String emailID
	String password
	
	@Field
	long departmentID
	char type = 'U' //U,S,A
	char status = 'A'//
	long customerID  
	String custCode // for ui specific, not in table
	String timeZone = 'America/Los_Angeles' 
	String listDepts 
	
	/*@Embedded
	UserInfo userInfo
	
	@Embedded
	UserNotification userNotification*/
	//List<MFAUserImage> mfaUserImage
	//MFAUserOTP mfaUserOTP
	//List<MFAUserQuestionnaire> mfaUserQuestionnaire
	String createdBy
	String updatedBy
/*	@JsonSerialize//(using = JsonDateTimeSerializer.Class)
	@JsonDeserialize//(using = JsonDateTimeDeserializer.class)
	Date lastLogin
	@JsonSerialize//(using = JsonDateTimeSerializer.class)
	@JsonDeserialize//(using = JsonDateTimeDeserializer.class)
	Date createDate
	@JsonSerialize//(using = JsonDateTimeSerializer.class)
	@JsonDeserialize//(using = JsonDateTimeDeserializer.class)
	Date updateDate*/
}

/*
@CompileStatic
@Canonical
@Embeddable
@Indexed
class UserInfo {
	String locale = "en";                 default - en
	//char approvalMandatory = 'N'         ///* default - N   //ZHPE-9770 - COMMENTED
	char sendNotification  = 'N'         default - N 
	//char allDepartments   = 'N'         //* default - N  //ZHPE-9770 - COMMENTED
	String oldpasswords           default - 1  
	Date expiryDate                
	int invalidCounter  =0           default - 0  
	long mobileNumber
	String resetToken  
	String rules            
	//int isForgotPassword = 0         //* default - 0 
}

@CompileStatic
@Canonical
@Embeddable
@Indexed
class UserNotification {
	char listStatus = 'N' 
	char detailedReportStatus= 'N' 
	String  campaignStatus = "Disable" 
	/** Notification Address List *//*
	String emailAddressList
}


@CompileStatic
@Canonical
class MFAUserImage {
	int imageID            
	String textPhrase    
	char isSelected
}

@CompileStatic
@Canonical
class MFAUserOTP {
	int OTP            
	Date expiryDate
}

@CompileStatic
@Canonical
class MFAUserQuestionnaire {
	int mfaquestionID
	String answer
}
*/