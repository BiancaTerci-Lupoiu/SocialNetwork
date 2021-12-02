package project.lab6.repository.file;

import project.lab6.domain.User;
import project.lab6.domain.validators.Validator;

import java.util.List;

public class UserFile extends AbstractFileRepository<Long, User> {
    public UserFile(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    /**
     * creates a User from a list of attributes
     *
     * @param attributes list with entity attributes
     * @return a User based on attributes
     */
    @Override
    protected User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));
        return user;
    }

    /**
     * @param entity
     * @return the string with the User attributes
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }
}
