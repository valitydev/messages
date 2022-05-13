package dev.vality.messages.domain.mapper;

import dev.vality.messages.domain.Message;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class MessageMapper {

    public static Message fromThrift(dev.vality.damsel.messages.Message messageThrift) {
        return Message.builder()
                .id(messageThrift.getMessageId())
                .text(messageThrift.getText())
                .userId(messageThrift.getUserId())
                .createdDate(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(messageThrift.getTimestamp())))
                .build();
    }

    public static dev.vality.damsel.messages.Message toThrift(Message message) {
        return new dev.vality.damsel.messages.Message(
                message.getId(),
                message.getText(),
                message.getUserId(),
                message.getCreatedDate().toString()
        );
    }

}
