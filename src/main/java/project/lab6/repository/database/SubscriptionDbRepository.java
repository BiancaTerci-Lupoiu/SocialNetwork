package project.lab6.repository.database;

import project.lab6.domain.TupleWithIdUserEvent;
import project.lab6.domain.entities.events.Subscription;
import project.lab6.repository.database.query.Query;
import project.lab6.repository.database.query.SaveQuery;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class SubscriptionDbRepository extends AbstractDbRepository<TupleWithIdUserEvent, Subscription> {
    public SubscriptionDbRepository(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    @Override
    protected Subscription getEntityFromSet(ResultSet set) throws SQLException {
        Long idUser = set.getLong("id_user");
        Long idEvent = set.getLong("id_event");
        LocalDateTime date = set.getTimestamp("date").toLocalDateTime();
        return new Subscription(idUser, idEvent, date);
    }

    @Override
    public Subscription findOne(TupleWithIdUserEvent id) {
        return genericFindOne(new Query() {
            @Override
            public String getSqlString() {
                return "select * from subscriptions where id_user=? and id_event=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id.getIdUser());
                statement.setLong(2, id.getIdEvent());
            }
        });
    }

    @Override
    public List<Subscription> findAll() {
        return genericFindAll("select * from subscriptions");
    }

    @Override
    public Subscription save(Subscription subscription) {
        if (findOne(subscription.getId()) != null)
            return null;
        return genericSave(subscription, new SaveQuery<>() {
            @Override
            public void setId(Subscription entity, Connection connection) throws SQLException {
            }

            @Override
            public String getSqlString() {
                return "insert into subscriptions (id_user,id_event,date) values (?,?,?)";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, subscription.getIdUser());
                statement.setLong(2, subscription.getIdEvent());
                statement.setTimestamp(3, Timestamp.valueOf(subscription.getDate()));
            }
        });
    }

    @Override
    public boolean delete(TupleWithIdUserEvent id) {
        return genericDelete(new Query() {
            @Override
            public String getSqlString() {
                return "delete from subscriptions where id_user=? and id_event=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id.getIdUser());
                statement.setLong(2, id.getIdEvent());
            }
        });
    }

    @Override
    public boolean update(Subscription subscription) {
        return genericUpdate(new Query() {
            @Override
            public String getSqlString() {
                return "update subscriptions set date=? where id_user=? and id_event=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setTimestamp(1, Timestamp.valueOf(subscription.getDate()));
                statement.setLong(2, subscription.getIdUser());
                statement.setLong(3, subscription.getIdEvent());
            }
        });
    }
}
