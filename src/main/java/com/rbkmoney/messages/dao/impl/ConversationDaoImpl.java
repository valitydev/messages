package com.rbkmoney.messages.dao.impl;

import com.rbkmoney.messages.dao.ConversationDao;
import com.rbkmoney.messages.domain.Conversation;
import com.rbkmoney.messages.domain.ConversationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.rbkmoney.messages.utils.DaoUtils.*;

@Component
@RequiredArgsConstructor
public class ConversationDaoImpl implements ConversationDao {

    private final JdbcTemplate jdbcTemplate;
    private final ConversationRowMapper rowMapper = new ConversationRowMapper();

    @Override
    public void saveAll(List<Conversation> conversations) {
        String upsertSql = "INSERT INTO msgs.conversation(id, status) VALUES (?, ?) " +
                "ON CONFLICT (id) DO " +
                "UPDATE SET status = excluded.status;";

        int[][] updateCounts = jdbcTemplate.batchUpdate(upsertSql, conversations, BATCH_SIZE, (ps, argument) -> {
            ps.setString(1, argument.getId());
            ps.setString(2, argument.getStatus().toString());
        });
        checkUpdateRowsSuccess(updateCounts);


    }

    @Override
    public List<Conversation> findAllById(List<String> conversationIds) {
             String selectSql = "SELECT id, status FROM msgs.conversation " +
                "WHERE id in " + collectToIdsCollection(conversationIds);

        return jdbcTemplate.query(selectSql, rowMapper);
    }

    private static class ConversationRowMapper implements RowMapper<Conversation> {
        @Override
        public Conversation mapRow(ResultSet rs, int i) throws SQLException {
            String id = rs.getString("id");
            ConversationStatus status = ConversationStatus.valueOf(rs.getString("status"));
            return new Conversation(id, List.of(), status);
        }
    }

}
