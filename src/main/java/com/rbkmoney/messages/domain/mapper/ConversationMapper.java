package com.rbkmoney.messages.domain.mapper;

import com.rbkmoney.messages.domain.Conversation;

import java.util.stream.Collectors;

public class ConversationMapper {

    public static Conversation fromThrift(com.rbkmoney.damsel.messages.Conversation conversationThrift) {
        return Conversation.builder()
                .id(conversationThrift.getConversationId())
                .messages(conversationThrift.getMessages().stream()
                        .map(MessageMapper::fromThrift)
                        .map(message -> message.withConversationId(conversationThrift.getConversationId()))
                        .collect(Collectors.toList()))
                .status(ConversationStatusMapper.fromThrift(conversationThrift.getStatus()))
                .build();
    }

    public static com.rbkmoney.damsel.messages.Conversation toThrift(Conversation conversation) {
        return new com.rbkmoney.damsel.messages.Conversation(
                conversation.getId(),
                conversation.getMessages().stream().map(MessageMapper::toThrift).collect(Collectors.toList()),
                ConversationStatusMapper.toThrift(conversation.getStatus())
        );
    }

}
