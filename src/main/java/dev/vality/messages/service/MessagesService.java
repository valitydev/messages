package dev.vality.messages.service;

import dev.vality.damsel.messages.ConversationFilter;
import dev.vality.damsel.messages.ConversationsNotFound;
import dev.vality.damsel.messages.GetConversationResponse;
import dev.vality.damsel.messages.MessageServiceSrv;
import dev.vality.messages.dao.ConversationDao;
import dev.vality.messages.dao.MessageDao;
import dev.vality.messages.dao.UserDao;
import dev.vality.messages.domain.Conversation;
import dev.vality.messages.domain.ConversationStatus;
import dev.vality.messages.domain.Message;
import dev.vality.messages.domain.User;
import dev.vality.messages.domain.mapper.ConversationMapper;
import dev.vality.messages.domain.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.vality.messages.domain.mapper.ConversationStatusMapper.fromThrift;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessagesService implements MessageServiceSrv.Iface {

    private final UserDao userDao;
    private final ConversationDao conversationDao;
    private final MessageDao messageDao;

    @Override
    public GetConversationResponse getConversations(List<String> conversationIds, ConversationFilter conversationFilter)
            throws TException {
        String conversationIdsJoin = String.join(", ", conversationIds);
        log.info("Get conversation by ids: {}", conversationIdsJoin);
        List<Conversation> conversations = conversationDao.findAllById(conversationIds);

        checkAllConversationsFound(conversations, conversationIds);

        if (conversationFilter != null && conversationFilter.isSetConversationStatus()) {
            ConversationStatus conversationStatus = fromThrift(conversationFilter.getConversationStatus());
            conversations.removeIf(conversation -> !conversation.getStatus().equals(conversationStatus));
        }

        log.info("Get messages by ids: {}", conversationIdsJoin);
        conversations = enrichWithMessages(conversations, conversationIds);

        log.info("Get users by ids: {}", conversationIdsJoin);
        List<User> users = getUsers(conversations);

        return convertToResponse(conversations, users);
    }

    @Override
    @Transactional
    public void saveConversations(
            List<dev.vality.damsel.messages.Conversation> conversationsThrift,
            dev.vality.damsel.messages.User user) throws TException {
        try {
            log.info("Save conversations for user: {}", user);
            List<Conversation> conversations = conversationsThrift.stream()
                    .map(ConversationMapper::fromThrift)
                    .collect(Collectors.toList());

            List<Message> messages = conversations.stream()
                    .flatMap(conversation -> conversation.getMessages().stream())
                    .collect(Collectors.toList());

            conversationDao.saveAll(conversations);
            userDao.save(UserMapper.fromThrift(user));
            messageDao.saveAll(messages);
        } catch (Exception e) {
            log.error("Failed to save conversation for user: {}", user, e);
            throw new TException(e);
        }
    }

    private GetConversationResponse convertToResponse(List<Conversation> conversations,
                                                      List<User> users) {
        List<dev.vality.damsel.messages.Conversation> conversationsList = conversations.stream()
                .map(ConversationMapper::toThrift)
                .collect(Collectors.toList());
        Map<String, dev.vality.damsel.messages.User> idsUsersMap = users.stream()
                .map(UserMapper::toThrift)
                .collect(Collectors.toMap(
                        dev.vality.damsel.messages.User::getUserId,
                        user -> user)
                );
        return new GetConversationResponse(
                conversationsList,
                idsUsersMap);
    }

    private List<User> getUsers(
            List<Conversation> conversations) {

        List<String> userIds = conversations.stream()
                .flatMap(conversation -> conversation.getMessages().stream())
                .map(Message::getUserId)
                .distinct()
                .collect(Collectors.toList());

        return userDao.findAllById(userIds);
    }

    private void checkAllConversationsFound(List<Conversation> conversations,
                                            List<String> conversationIds) throws ConversationsNotFound {
        if (conversations.size() == conversationIds.size()) {
            return;
        }

        List<String> foundIds = conversations.stream()
                .map(Conversation::getId)
                .collect(Collectors.toList());

        List<String> notFoundIds = ListUtils.removeAll(conversationIds, foundIds);

        throw new ConversationsNotFound(notFoundIds);
    }

    private List<Conversation> enrichWithMessages(
            List<Conversation> conversations,
            List<String> conversationIds) {
        Map<String, List<Message>> conversationIdsMessages = messageDao.findAllByConversationId(conversationIds)
                .stream()
                .collect(Collectors.groupingBy(Message::getConversationId));
        return conversations.stream()
                .map(conversation ->
                        conversation.withMessages(
                                conversationIdsMessages.getOrDefault(conversation.getId(), List.of())
                        )
                )
                .collect(Collectors.toList());
    }

}
