package com.rbkmoney.messages.domain.mapper;

import com.rbkmoney.messages.domain.ConversationStatus;

public class ConversationStatusMapper {

    public static ConversationStatus fromThrift(com.rbkmoney.damsel.messages.ConversationStatus conversationStatusThrift) {
        switch (conversationStatusThrift) {
            case ACTUAL:
                return ConversationStatus.ACTUAL;
            case OUTDATED:
                return ConversationStatus.OUTDATED;
            default:
                throw new IllegalStateException("Unknown status: " + conversationStatusThrift);
        }
    }

    public static com.rbkmoney.damsel.messages.ConversationStatus toThrift(ConversationStatus conversationStatus) {
        switch (conversationStatus) {
            case ACTUAL:
                return com.rbkmoney.damsel.messages.ConversationStatus.ACTUAL;
            case OUTDATED:
                return com.rbkmoney.damsel.messages.ConversationStatus.OUTDATED;
            default:
                throw new IllegalStateException("Unknown status: " + conversationStatus);
        }
    }
}
