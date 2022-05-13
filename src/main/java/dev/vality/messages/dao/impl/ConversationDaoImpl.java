package dev.vality.messages.dao.impl;

import dev.vality.messages.dao.ConversationDao;
import dev.vality.messages.domain.Conversation;
import dev.vality.messages.domain.ConversationStatus;
import dev.vality.messages.exception.DaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConversationDaoImpl implements ConversationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final ConversationRowMapper rowMapper = new ConversationRowMapper();

    @Override
    public void saveAll(List<Conversation> conversations) throws DaoException {
        String upsertSql = "INSERT INTO msgs.conversation(id, status) VALUES (:id, :status) " +
                "ON CONFLICT (id) DO " +
                "UPDATE SET status = excluded.status;";
        try {
            var batchValues = conversations.stream()
                    .map(c -> new MapSqlParameterSource()
                            .addValue("id", c.getId())
                            .addValue("status", c.getStatus().toString()).getValues())
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(upsertSql, batchValues.toArray(new Map[conversations.size()]));
        } catch (Exception ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public List<Conversation> findAllById(List<String> conversationIds) throws DaoException {
        String selectSql = "SELECT id, status FROM msgs.conversation " +
                "WHERE id in (:ids)";
        try {
            return jdbcTemplate.query(selectSql, new MapSqlParameterSource("ids", conversationIds), rowMapper);
        } catch (Exception ex) {
            throw new DaoException(ex);
        }
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
