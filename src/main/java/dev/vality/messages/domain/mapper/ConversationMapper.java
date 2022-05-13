package dev.vality.messages.domain.mapper;

import dev.vality.messages.domain.Conversation;

import java.util.stream.Collectors;

public class ConversationMapper {

    public static Conversation fromThrift(dev.vality.damsel.messages.Conversation conversationThrift) {
        return Conversation.builder()
                .id(conversationThrift.getConversationId())
                .messages(conversationThrift.getMessages().stream()
                        .map(MessageMapper::fromThrift)
                        .map(message -> message.withConversationId(conversationThrift.getConversationId()))
                        .collect(Collectors.toList()))
                .status(ConversationStatusMapper.fromThrift(conversationThrift.getStatus()))
                .build();
    }

    public static dev.vality.damsel.messages.Conversation toThrift(Conversation conversation) {
        return new dev.vality.damsel.messages.Conversation(
                conversation.getId(),
                conversation.getMessages().stream().map(MessageMapper::toThrift).collect(Collectors.toList()),
                ConversationStatusMapper.toThrift(conversation.getStatus())
        );
    }

}
