package sakuuj.learn.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import sakuuj.learn.library.models.Book;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Component
public class BookDao {

    final
    JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL = """
            SELECT *
            FROM Book""";
    private static final String SELECT_BY_ID = """
            SELECT *
            FROM Book
            WHERE id = ?""";

    private static final String INSERT = """
            INSERT INTO
            Book(name, authorName, yearOfPublishing)
            VALUES(?, ?, ?)""";

    private static final String UPDATE_BY_ID = """
            UPDATE Book
            SET (name, authorName, yearOfPublishing, personId)
             = (?, ?, ?, ?)
             WHERE id = ?""";

    private static final String SELECT_BY_PERSON_ID = """
            SELECT *
            FROM Book
            WHERE personId = ?""";

    private static final String SELECT_PERSON_ID_IS_NULL = """
            SELECT *
            FROM Book
            WHERE personId IS NULL""";

    private static final String DELETE_BY_ID = """
            DELETE
            FROM Book
            WHERE id = ?""";

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Book> selectAll() {
        return  jdbcTemplate.query(SELECT_ALL,
                new BeanPropertyRowMapper<>(Book.class));
    }

    public Optional<Book> selectById(int id) {
        return jdbcTemplate.query(SELECT_BY_ID,
                new BeanPropertyRowMapper<>(Book.class),
                id).stream().findAny();
    }

    public List<Book> selectPersonIdIsNull() {
        return jdbcTemplate.query(SELECT_PERSON_ID_IS_NULL,
                new BeanPropertyRowMapper<>(Book.class));
    }
    public List<Book> selectByPersonId(Integer personId) {
        return jdbcTemplate.query(SELECT_BY_PERSON_ID,
                new BeanPropertyRowMapper<>(Book.class),
                personId);
    }
    public void updateById(Book book) {
        jdbcTemplate.update(UPDATE_BY_ID,
                book.getName(),
                book.getAuthorName(),
                book.getYearOfPublishing(),
                book.getPersonId(),
                book.getId());
    }

    public void insert(Book book) {
        jdbcTemplate.update(INSERT,
                book.getName(),
                book.getAuthorName(),
                book.getYearOfPublishing());
    }

    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_BY_ID,
                id);
    }
}
