package dev.vality.messages.service;

import dev.vality.damsel.messages.ConversationsNotFound;
import dev.vality.damsel.messages.GetConversationResponse;
import dev.vality.messages.TestData;
import dev.vality.messages.dao.ConversationDao;
import dev.vality.messages.dao.MessageDao;
import dev.vality.messages.dao.UserDao;
import dev.vality.messages.domain.mapper.ConversationMapper;
import dev.vality.messages.domain.mapper.UserMapper;
import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                messagesService.getConversations(ids, TestData.CONVERSATION_FILTER_ACTUAL).getConversations().size());
    }

    @Test
    public void conversationsSavedNormally() throws TException {
        messagesService.saveConversations(
                TestData.TEST_CONVERSATIONS.stream().map(ConversationMapper::toThrift).collect(Collectors.toList()),
                UserMapper.toThrift(TestData.TEST_USER));

        verify(conversationDao, times(1))
                .saveAll(TestData.TEST_CONVERSATIONS);
        verify(userDao, times(1))
                .save(TestData.TEST_USER);
        verify(messageDao, times(1))
                .saveAll(TestData.TEST_MESSAGES);
    }

    private void givenConversations() {
        when(conversationDao.findAllById(anyList()))
                .thenReturn(new ArrayList<>(TestData.TEST_CONVERSATIONS));
    }

    private void givenUsers() {
        when(userDao.findAllById(anyList()))
                .thenReturn(List.of(TestData.TEST_USER));
    }

    private void givenMessages() {
        when(messageDao.findAllByConversationId(anyList()))
                .thenReturn(new ArrayList<>(TestData.TEST_MESSAGES));
    }


}
