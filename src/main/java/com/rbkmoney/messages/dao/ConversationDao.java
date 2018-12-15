package com.rbkmoney.messages.dao;

import com.rbkmoney.messages.domain.Conversation;

import java.util.List;

public interface ConversationDao {

    void saveAll(List<Conversation> conversations);

    List<Conversation> findAllById(List<String> conversationIds);

}
