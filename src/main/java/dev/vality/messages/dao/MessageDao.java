package dev.vality.messages.dao;

import dev.vality.messages.domain.Message;
import dev.vality.messages.exception.DaoException;

import java.util.List;

public interface MessageDao {

    void saveAll(List<Message> messages) throws DaoException;

    List<Message> findAllByConversationId(List<String> conversationIds) throws DaoException;
}
