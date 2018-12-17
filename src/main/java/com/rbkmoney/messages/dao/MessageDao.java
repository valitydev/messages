package com.rbkmoney.messages.dao;

import com.rbkmoney.messages.domain.Message;
import com.rbkmoney.messages.exception.DaoException;

import java.util.List;

public interface MessageDao {

    void saveAll(List<Message> messages) throws DaoException;

    List<Message> findAllByConversationId(List<String> conversationIds) throws DaoException;
}
