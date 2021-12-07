package project.lab6.repository.file;

import project.lab6.domain.User;
import project.lab6.domain.validators.Validator;

import java.util.ArrayList;
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
        return new User(Long.parseLong(attributes.get(0)), attributes.get(1), attributes.get(2),attributes.get(3),
                attributes.get(4),attributes.get(5));
    }

    /**
     * @param entity
     * @return the string with the User attributes
     */
    @Override
    protected String createEntityAsString(User entity) {
        return String.format("%s;%s;%s;%s;%s;%s",entity.getId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getHashPassword(),
                entity.getSalt());
    }
}
