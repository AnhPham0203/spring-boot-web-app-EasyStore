package com.apn.ecomercAP.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apn.ecomercAP.model.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {
	UserDtls findByEmail(String email);

	List<UserDtls> findByRole(String role);

	Boolean existsByEmail(String email);
}
