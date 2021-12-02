package project.lab6.repository.file;

import project.lab6.domain.Friendship;
import project.lab6.domain.Status;
import project.lab6.domain.Tuple;
import project.lab6.domain.validators.Validator;
import project.lab6.utils.Constants;

import java.time.LocalDate;
import java.util.List;

public class FriendshipFile extends AbstractFileRepository<Tuple<Long, Long>, Friendship> {
    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    /**
     * creates a Friendship from a list of attributes
     *
     * @param attributes list with entity attributes
     * @return a Friendship based on attributes
     */
    @Override
    protected Friendship extractEntity(List<String> attributes) {
        Friendship friendship;
        friendship = new Friendship(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1)),
                LocalDate.parse(attributes.get(2), Constants.DATE_FORMATTER), Status.valueOf(attributes.get(3)));
        return friendship;
    }

    /**
     * @param entity
     * @return the string with the Friendship attributes
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().getLeft() + ";" + entity.getId().getRight() + ";" + entity.getDate().format(Constants.DATE_FORMATTER) + ";" + entity.getStatus();
    }
}
