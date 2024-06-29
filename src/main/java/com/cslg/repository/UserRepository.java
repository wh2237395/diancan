package com.cslg.repository;

import com.cslg.bean.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 编程小石头：2501902696（微信）
 */
public interface UserRepository extends JpaRepository<UserInfo, String> {
    UserInfo findByOpenid(String openid);
}
