package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {

    private JdbcTemplate jdbcTemplate = null;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        final String sql = "Insert into person values(?,?)";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setObject(1,id, Types.OTHER);
                ps.setString(2,person.getName());

                return ps.executeUpdate();
            }
        });
    }

    @Override
    public List<Person> selectAllPeople() {
        final String sql = "SELECT id,name FROM person";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            return new Person(
                    UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("name"));
        });
    }

    @Override
    public Optional<Person> selectPersonByID(UUID id) {
        final String sql = "SELECT id,name from person where id=?";
        Person person = jdbcTemplate.queryForObject(sql,new Object[]{id}, (resultSet, i) -> {
            return new Person(UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("name"));
        });
        return Optional.ofNullable(person);
    }

    @Override
    public int deletePersonByID(UUID id) {
        String sql = "DELETE FROM person WHERE id=?";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setObject(1,id,Types.OTHER);

                return preparedStatement.executeUpdate();
            }
        });
    }

    @Override
    public int updatePersonById(UUID id, Person person) {
        String sql = "UPDATE person SET name=? WHERE id=?";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1,person.getName());
                preparedStatement.setObject(2,id,Types.OTHER);

                return preparedStatement.executeUpdate();
            }
        });
    }
}
