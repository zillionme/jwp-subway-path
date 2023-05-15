package subway.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.LineEntity;

@Component
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(LineEntity lineEntity) {
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", lineEntity.getId())
                .addValue("name", lineEntity.getName())
                .addValue("color", lineEntity.getColor());

        return insertAction.executeAndReturnKey(source).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    //todo 찾아볼 것 : queryForObject이 null을 반환하는 경우가 무엇인지?
    public Optional<LineEntity> findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        }
        catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(LineEntity newLineEntity) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLineEntity.getName(), newLineEntity.getColor(), newLineEntity.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

    public Optional<LineEntity> findByName(String name) {
        String sql = "select id, name, color from LINE WHERE name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        }
        catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByColor(String color) {
        String sql = "select id, name, color from LINE WHERE color = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, color));
        }
        catch (DataAccessException e) {
            return Optional.empty();
        }
    }

}
