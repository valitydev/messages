package dev.vality.messages;

import dev.vality.damsel.messages.ConversationFilter;
import dev.vality.messages.domain.Conversation;
import dev.vality.messages.domain.ConversationStatus;
import dev.vality.messages.domain.Message;
import dev.vality.messages.domain.User;
import org.springframework.data.util.Pair;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestData {

    public static final List<Conversation> TEST_CONVERSATIONS = List.of(
            createConversation("1", ConversationStatus.ACTUAL)
                    .withMessages(createMessages(List.of(
                            Pair.of("1", "1"),
                            Pair.of("2", "1"),
                            Pair.of("3", "2")
                    ), "1")),
            createConversation("2", ConversationStatus.ACTUAL),
            createConversation("3", ConversationStatus.OUTDATED)
                    .withMessages(createMessages(List.of(
                            Pair.of("4", "1"),
                            Pair.of("5", "1"),
                            Pair.of("6", "2")
                    ), "3")),
            createConversation("4", ConversationStatus.OUTDATED)
    );

    public static final User TEST_USER = createUser("1");

    public static final List<Message> TEST_MESSAGES = TEST_CONVERSATIONS.stream()
            .flatMap(conv -> conv.getMessages().stream())
            .collect(Collectors.toList());


    public static final ConversationFilter CONVERSATION_FILTER_ACTUAL = new ConversationFilter()
            .setConversationStatus(dev.vality.damsel.messages.ConversationStatus.ACTUAL);

    public static Conversation createConversation(String id, ConversationStatus status) {
        return new Conversation(id, new ArrayList<>(), status);
    }

    public static List<Message> createMessages(List<Pair<String, String>> messageUserIds, String conversationId) {
        return messageUserIds.stream()
                .map(messageUserId ->
                        createMessage(messageUserId.getFirst(), conversationId, messageUserId.getSecond()))
                .collect(Collectors.toList());
    }

    public static Message createMessage(String id, String conversationId, String userId) {
        return new Message(
                id, "bla-bla-bla", Instant.now().truncatedTo(ChronoUnit.SECONDS), conversationId, userId
        );
    }

    public static User createUser(String id) {
        return new User(id, "test@mail.com", "Ivan Ivanov");
    }
}
