package com.apn.ecomercAP.service;

import com.apn.ecomercAP.model.UserDtls;

public interface UserService {
	public UserDtls saveUser(UserDtls user);
	
	public Boolean checkExitsEmail(String email);
	
	public void increaseFailedAttempt(UserDtls user);

	public void userAccountLock(UserDtls user);

	public boolean unlockAccountTimeExpired(UserDtls user);
	
	public UserDtls getUserByEmail(String email);
}
