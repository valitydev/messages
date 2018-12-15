package com.rbkmoney.messages.dao.impl;

import com.rbkmoney.messages.dao.UserDao;
import com.rbkmoney.messages.domain.User;
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
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    private final UserRowMapper rowMapper = new UserRowMapper();

    @Override
    public void saveAll(List<User> users) {
        String upsertSql = "INSERT INTO msgs.author(id, email, full_name) VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO " +
                "UPDATE set email = excluded.email, full_name = excluded.full_name;";

        int[][] updateCounts = jdbcTemplate.batchUpdate(upsertSql, users, BATCH_SIZE, (ps, argument) -> {
            ps.setString(1, argument.getId());
            ps.setString(2, argument.getEmail());
            ps.setString(3, argument.getFullName());
        });
        checkUpdateRowsSuccess(updateCounts);
    }

    @Override
    public List<User> findAllById(List<String> ids) {
        String selectSql = "SELECT id, email, full_name FROM msgs.author " +
                "WHERE id in " + collectToIdsCollection(ids);

        return jdbcTemplate.query(selectSql, rowMapper);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            String id = rs.getString("id");
            String email = rs.getString("email");
            String fullName = rs.getString("full_name");
            return new User(id, email, fullName);
        }
    }
}
