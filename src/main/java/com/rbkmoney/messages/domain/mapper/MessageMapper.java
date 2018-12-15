package com.rbkmoney.messages.domain.mapper;

import com.rbkmoney.messages.domain.Message;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class MessageMapper {

    public static Message fromThrift(com.rbkmoney.damsel.messages.Message messageThrift) {
        return Message.builder()
                .id(messageThrift.getMessageId())
                .text(messageThrift.getText())
                .userId(messageThrift.getUserId())
                .createdDate(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(messageThrift.getTimestamp())))
                .build();
    }

    public static com.rbkmoney.damsel.messages.Message toThrift(Message message) {
        return new com.rbkmoney.damsel.messages.Message(
                message.getId(),
                message.getText(),
                message.getUserId(),
                message.getCreatedDate().toString()
        );
    }

}
