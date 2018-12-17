package com.rbkmoney.messages.service;

import com.rbkmoney.damsel.messages.ConversationsNotFound;
import com.rbkmoney.damsel.messages.GetConversationResponse;
import com.rbkmoney.damsel.messages.UsersNotProvided;
import com.rbkmoney.messages.dao.ConversationDao;
import com.rbkmoney.messages.dao.MessageDao;
import com.rbkmoney.messages.dao.UserDao;
import com.rbkmoney.messages.domain.mapper.ConversationMapper;
import com.rbkmoney.messages.domain.mapper.UserMapper;
import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rbkmoney.messages.TestData.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class MessagesServiceTest {

    private final ConversationDao conversationDao = mock(ConversationDao.class);
    private final UserDao userDao = mock(UserDao.class);
    private final MessageDao messageDao = mock(MessageDao.class);

    private final MessagesService messagesService = new MessagesService(userDao, conversationDao, messageDao);

    @Test
    public void allConversationsFound() throws TException {
        givenConversations();
        givenUsers();
        givenMessages();

        List<String> ids = List.of("1", "2", "3", "4");
        GetConversationResponse response = messagesService.getConversations(ids, null);

        Assert.assertEquals(ids.size(), response.getConversations().size());
        Assert.assertEquals(TEST_USERS.size(), response.getUsers().size());
    }

    @Test
    public void notFoundConversationsIdsReturnedInException() throws TException {
        givenConversations();
        givenUsers();
        givenMessages();

        String cannotBeFound = "CannotBeFound";
        List<String> idsWithNotFound = List.of("1", "2", "3", "4", cannotBeFound);

        try {
            messagesService.getConversations(idsWithNotFound, null);
        } catch (ConversationsNotFound ex) {
            Assert.assertEquals(cannotBeFound, ex.getIds().get(0));
        }
    }

    @Test
    public void conversationsFiltered() throws TException {
        givenConversations();
        givenUsers();
        givenMessages();

        List<String> ids = List.of("1", "2", "3", "4");

        Assert.assertEquals(2,
                messagesService.getConversations(ids, CONVERSATION_FILTER_ACTUAL).getConversations().size());
    }

    @Test
    public void conversationsSavedNormally() throws TException {
        messagesService.saveConversations(
                TEST_CONVERSATIONS.stream().map(ConversationMapper::toThrift).collect(Collectors.toList()),
                TEST_USERS.stream().map(UserMapper::toThrift).collect(Collectors.toList()));

        verify(conversationDao, times(1))
                .saveAll(TEST_CONVERSATIONS);
        verify(userDao, times(1))
                .saveAll(TEST_USERS);
        verify(messageDao, times(1))
                .saveAll(TEST_MESSAGES);
    }

    @Test
    public void notProvidedUsersWhenSaving() throws TException {
        try {
            messagesService.saveConversations(
                    TEST_CONVERSATIONS.stream().map(ConversationMapper::toThrift).collect(Collectors.toList()),
                    TEST_USERS.stream()
                            .map(UserMapper::toThrift)
                            .filter(user -> !user.getUserId().equals("1"))
                            .collect(Collectors.toList()));
        } catch (UsersNotProvided ex) {
            Assert.assertEquals("1", ex.getIds().get(0));
        }
    }

    private void givenConversations() {
        when(conversationDao.findAllById(anyList()))
                .thenReturn(new ArrayList<>(TEST_CONVERSATIONS));
    }

    private void givenUsers() {
        when(userDao.findAllById(anyList()))
                .thenReturn(new ArrayList<>(TEST_USERS));
    }

    private void givenMessages() {
        when(messageDao.findAllByConversationId(anyList()))
                .thenReturn(new ArrayList<>(TEST_MESSAGES));
    }


}
