package dev.vality.messages.domain.mapper;

import dev.vality.messages.domain.Conversation;
import dev.vality.messages.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(BlockJUnit4ClassRunner.class)
public class MappersTest {

    @Test
    public void testAllChainOfMappers() {
        List<Conversation> collect = TestData.TEST_CONVERSATIONS.stream()
                .map(ConversationMapper::toThrift)
                .map(ConversationMapper::fromThrift)
                .collect(Collectors.toList());
        Assert.assertTrue(collect.containsAll(TestData.TEST_CONVERSATIONS));
    }

}
