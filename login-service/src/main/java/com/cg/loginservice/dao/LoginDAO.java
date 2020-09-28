package com.cg.loginservice.dao;

import com.cg.loginservice.entity.LoginCredentials;
import com.cg.loginservice.id.LoginCredentialsId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDAO extends JpaRepository<LoginCredentials, LoginCredentialsId> {
	@Query("SELECT p from LoginCredentials p  where p.userName= :userName AND p.password= :pass AND p.role= :role")
    LoginCredentials validateUser(@Param("userName")String userName,@Param("pass") String pass, @Param("role") String role);
}