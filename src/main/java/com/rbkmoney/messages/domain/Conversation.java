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
    private String id;

    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    private ConversationStatus status;

    public Conversation withMessages(List<Message> messages) {
        this.setMessages(messages);
        return this;
    }
}
