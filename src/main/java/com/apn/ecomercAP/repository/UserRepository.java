package com.apn.ecomercAP.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apn.ecomercAP.model.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {
	UserDtls findByEmail(String email);

	Boolean existsByEmail(String email);
}
