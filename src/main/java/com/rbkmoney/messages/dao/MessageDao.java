package com.rbkmoney.messages.dao;

import com.rbkmoney.messages.domain.Message;

import java.util.List;

public interface MessageDao {

    void saveAll(List<Message> messages);

    List<Message> findAllByConversationId(List<String> conversationIds);
}
