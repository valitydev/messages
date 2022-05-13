package dev.vality.messages.dao.impl;

import dev.vality.messages.dao.UserDao;
import dev.vality.messages.domain.User;
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
public class UserDaoImpl implements UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final UserRowMapper rowMapper = new UserRowMapper();

    @Override
    public void saveAll(List<User> users) throws DaoException {
        String upsertSql = "INSERT INTO msgs.author(id, email, full_name) VALUES (:id, :email, :full_name) " +
                "ON CONFLICT (id) DO " +
                "UPDATE set email = excluded.email, full_name = excluded.full_name;";
        try {

            var batchValues = users.stream()
                    .map(u -> new MapSqlParameterSource()
                            .addValue("id", u.getId())
                            .addValue("email", u.getEmail())
                            .addValue("full_name", u.getFullName())
                            .getValues())
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(upsertSql, batchValues.toArray(new Map[users.size()]));
        } catch (Exception ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public void save(User user) throws DaoException {
        String insertSql = "INSERT INTO msgs.author(id, email, full_name) VALUES (:id, :email, :full_name) " +
                "ON CONFLICT (id) DO " +
                "UPDATE set email = excluded.email, full_name = excluded.full_name";
        try {
            jdbcTemplate.update(insertSql, new MapSqlParameterSource()
                    .addValue("id", user.getId())
                    .addValue("email", user.getEmail())
                    .addValue("full_name", user.getFullName()).getValues());

        } catch (Exception ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public List<User> findAllById(List<String> ids) throws DaoException {
        String selectSql = "SELECT id, email, full_name FROM msgs.author " +
                "WHERE id in (:ids)";
        try {
            return jdbcTemplate.query(selectSql, new MapSqlParameterSource("ids", ids), rowMapper);
        } catch (Exception ex) {
            throw new DaoException(ex);
        }
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
