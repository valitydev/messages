package dev.vality.messages.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Message {

    @Id
    private String id;

    private String text;

    private Instant createdDate;

    private String conversationId;

    private String userId;

    public Message withConversationId(String conversationId) {
        this.setConversationId(conversationId);
        return this;
    }

    public Message withUserId(String userId) {
        this.setUserId(userId);
        return this;
    }

}
