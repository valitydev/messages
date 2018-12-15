package com.rbkmoney.messages.dao.impl;

import com.rbkmoney.messages.dao.MessageDao;
import com.rbkmoney.messages.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static com.rbkmoney.messages.utils.DaoUtils.*;

@Component
@RequiredArgsConstructor
public class MessageDaoImpl implements MessageDao {

    private final JdbcTemplate jdbcTemplate;

    private final MessageRowMapper rowMapper = new MessageRowMapper();

    @Override
    public void saveAll(List<Message> messages) {
        String insertSql = "INSERT INTO msgs.message(id, text, created_date, conversation_id, user_id) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO NOTHING;"; // immutable messages history!

        int[][] updateCounts = jdbcTemplate.batchUpdate(insertSql, messages, BATCH_SIZE, (ps, argument) -> {
            ps.setString(1, argument.getId());
            ps.setString(2, argument.getText());
            ps.setTimestamp(3, Timestamp.from(argument.getCreatedDate()));
            ps.setString(4, argument.getConversationId());
            ps.setString(5, argument.getUserId());
        });
        checkUpdateRowsSuccess(updateCounts);
    }

    @Override
    public List<Message> findAllByConversationId(List<String> conversationIds) {
        String selectSql = "SELECT id, text, created_date, conversation_id, user_id FROM msgs.message " +
                "WHERE conversation_id in " + collectToIdsCollection(conversationIds);

        return jdbcTemplate.query(selectSql, rowMapper);
    }

    private static class MessageRowMapper implements RowMapper<Message> {
        @Override
        public Message mapRow(ResultSet rs, int i) throws SQLException {
            String id = rs.getString("id");
            String text = rs.getString("text");
            Instant createdDate = rs.getTimestamp("created_date").toInstant();
            String conversationId = rs.getString("conversation_id");
            String userId = rs.getString("user_id");
            return new Message(id, text, createdDate, conversationId, userId);
        }
    }

}
