package com.cg.loginservice.dao;

import com.cg.loginservice.entity.UserOTP;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOTPDAO extends JpaRepository<UserOTP, String>{
    
}
