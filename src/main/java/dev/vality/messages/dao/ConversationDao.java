package dev.vality.messages.dao;

import dev.vality.messages.domain.Conversation;
import dev.vality.messages.exception.DaoException;

import java.util.List;

public interface ConversationDao {

    void saveAll(List<Conversation> conversations) throws DaoException;

    List<Conversation> findAllById(List<String> conversationIds) throws DaoException;

}
