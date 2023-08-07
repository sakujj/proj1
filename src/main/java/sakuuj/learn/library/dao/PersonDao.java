package sakuuj.learn.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import sakuuj.learn.library.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDao {
    private static final String SELECT_ALL = """
            SELECT *
            FROM Person""";
    private static final String SELECT_BY_ID = """
            SELECT *
            FROM Person
            WHERE id = ?""";
    private static final String DELETE_BY_ID = """
            DELETE
            FROM Person
            WHERE id = ?""";
    private static final String UPDATE_BY_ID = """
            UPDATE Person
            SET (name, yearOfBirth) = (?, ?)
            WHERE id = ?""";
    private static final String INSERT = """
            INSERT INTO
            Person (name, yearOfBirth)
            VALUES(?, ?)""";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> selectAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> selectById(int id) {
        return jdbcTemplate.query(SELECT_BY_ID,
                        new BeanPropertyRowMapper<>(Person.class),
                        id)
                .stream().findAny();
    }

    public void insert(Person person) {

        jdbcTemplate.update(INSERT,
                person.getName(),
                person.getYearOfBirth());
    }
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    public void updateById(Person person) {
        jdbcTemplate.update(UPDATE_BY_ID,
                person.getName(),
                person.getYearOfBirth(),
                person.getId());
    }
}
