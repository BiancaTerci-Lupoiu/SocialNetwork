package project.lab6.repository.database;

import project.lab6.domain.entities.events.Event;
import project.lab6.repository.database.query.Query;
import project.lab6.repository.database.query.SaveQuery;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class EventDbRepository extends AbstractDbRepository<Long, Event> {
    public EventDbRepository(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    @Override
    public List<Event> findAll() {
        return genericFindAll("select * from events");
    }

    @Override
    public Event findOne(Long id) {
        return genericFindOne(new Query() {
            @Override
            public String getSqlString() {
                return "select * from events where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id);
            }
        });
    }

    @Override
    public Event save(Event event) {
        return genericSave(event, new SaveQuery<>() {
            @Override
            public void setId(Event entity, Connection connection) throws SQLException {
                Long id = getLongId(connection, "events", "id");
                entity.setId(id);
            }

            @Override
            public String getSqlString() {
                return "insert into events(id_owner,title,description,date) values" +
                        "(?,?,?,?)";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, event.getIdUserOwner());
                statement.setString(2, event.getTitle());
                if (event.getDescription() == null)
                    statement.setNull(3, Types.INTEGER);
                else
                    statement.setString(3, event.getDescription());
                statement.setTimestamp(4, Timestamp.valueOf(event.getDate()));
            }
        });
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    /**
     * Updates the event with a new title, description and date. Cant update the id of the owner
     *
     * @param entity entity must not be null
     */
    @Override
    public boolean update(Event entity) {
        return genericUpdate(new Query() {
            @Override
            public String getSqlString() {
                return "update events set title=?,description=?,date=? where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, entity.getTitle());
                statement.setString(2, entity.getDescription());
                statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
                statement.setLong(4, entity.getId());
            }
        });
    }

    @Override
    protected Event getEntityFromSet(ResultSet set) throws SQLException {
        Long id = set.getLong("id");
        Long idUserOwner = set.getLong("id_owner");
        String title = set.getString("title");
        String description = set.getString("description");
        LocalDateTime date = set.getTimestamp("date").toLocalDateTime();
        return new Event(id, idUserOwner, title, description, date);
    }
}
