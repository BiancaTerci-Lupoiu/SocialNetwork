package socialnetwork.domain.validators;

import socialnetwork.domain.User;

public class UserValidator implements Validator<User> {
    /**
     * verify if an User is valid
     *
     * @param entity - the entity to be validated (User)
     * @throws ValidationException if the id<=0 or
     *                             firstName is null or does not start with a capital letter or
     *                             lastName is null or does not start with a capital letter
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String errors = "";
        if (entity.getId() <= 0)
            errors += "invalid id!\n";
        String firstName = entity.getFirstName();
        if (firstName == null || firstName.isEmpty() || firstName.charAt(0) < 'A' || firstName.charAt(0) > 'Z')
            errors += "invalid firstName!\n";
        String lastName = entity.getLastName();
        if (lastName == null || lastName.isEmpty() || lastName.charAt(0) < 'A' || lastName.charAt(0) > 'Z')
            errors += "invalid lastName!\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}