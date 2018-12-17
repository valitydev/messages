package com.rbkmoney.messages.dao;

import com.rbkmoney.messages.domain.Conversation;
import com.rbkmoney.messages.exception.DaoException;

import java.util.List;

public interface ConversationDao {

    void saveAll(List<Conversation> conversations) throws DaoException;

    List<Conversation> findAllById(List<String> conversationIds) throws DaoException;

}
