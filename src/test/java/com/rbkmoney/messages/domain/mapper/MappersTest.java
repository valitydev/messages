package com.rbkmoney.messages.domain.mapper;

import com.rbkmoney.messages.domain.Conversation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

import static com.rbkmoney.messages.TestData.TEST_CONVERSATIONS;

@RunWith(BlockJUnit4ClassRunner.class)
public class MappersTest {

    @Test
    public void testAllChainOfMappers() {
        List<Conversation> collect = TEST_CONVERSATIONS.stream()
                .map(ConversationMapper::toThrift)
                .map(ConversationMapper::fromThrift)
                .collect(Collectors.toList());
        Assert.assertTrue(collect.containsAll(TEST_CONVERSATIONS));
    }

}
