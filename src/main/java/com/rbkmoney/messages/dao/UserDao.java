package com.rbkmoney.messages.dao;

import com.rbkmoney.messages.domain.User;

import java.util.List;

public interface UserDao {

    void saveAll(List<User> users);

    List<User> findAllById(List<String> ids);
}
