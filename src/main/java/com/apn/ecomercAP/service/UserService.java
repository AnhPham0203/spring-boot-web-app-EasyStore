package com.apn.ecomercAP.service;

import java.util.List;

import com.apn.ecomercAP.model.UserDtls;

public interface UserService {
	public UserDtls saveUser(UserDtls user);
	
	public UserDtls saveAdmin(UserDtls user);
	
	public Boolean checkExitsEmail(String email);
	
	public void increaseFailedAttempt(UserDtls user);

	public void userAccountLock(UserDtls user);

	public boolean unlockAccountTimeExpired(UserDtls user);
	
	public UserDtls getUserByEmail(String email);
	
	public List<UserDtls> getUserByRole(String role);
	
	public boolean changeStatus(Integer id, Boolean status);
}
