package dev.vality.messages.dao;

import dev.vality.messages.domain.User;
import dev.vality.messages.exception.DaoException;

import java.util.List;

public interface UserDao {

    void saveAll(List<User> users) throws DaoException;

    void save(User user) throws DaoException;

    List<User> findAllById(List<String> ids) throws DaoException;
}
