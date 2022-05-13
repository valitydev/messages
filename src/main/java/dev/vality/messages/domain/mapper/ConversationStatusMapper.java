package dev.vality.messages.domain.mapper;

import dev.vality.messages.domain.ConversationStatus;

public class ConversationStatusMapper {

    public static ConversationStatus fromThrift(dev.vality.damsel.messages.ConversationStatus conversationStatusThrift) {
        switch (conversationStatusThrift) {
            case ACTUAL:
                return ConversationStatus.ACTUAL;
            case OUTDATED:
                return ConversationStatus.OUTDATED;
            default:
                throw new IllegalStateException("Unknown status: " + conversationStatusThrift);
        }
    }

    public static dev.vality.damsel.messages.ConversationStatus toThrift(ConversationStatus conversationStatus) {
        switch (conversationStatus) {
            case ACTUAL:
                return dev.vality.damsel.messages.ConversationStatus.ACTUAL;
            case OUTDATED:
                return dev.vality.damsel.messages.ConversationStatus.OUTDATED;
            default:
                throw new IllegalStateException("Unknown status: " + conversationStatus);
        }
    }
}
