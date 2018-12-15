package com.rbkmoney.messages.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Conversation {

    @Id
    String id;

    @Builder.Default
    List<Message> messages = new ArrayList<>();

    ConversationStatus status;

    public Conversation withMessages(List<Message> messages) {
        this.setMessages(messages);
        return this;
    }
}
