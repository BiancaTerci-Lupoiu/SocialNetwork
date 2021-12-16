package project.lab6.domain.validators;

import project.lab6.domain.User;

public class UserValidator implements Validator<User> {
    /**
     * verify if an User is valid
     *
     * @param entity - the entity to be validated (User)
     * @throws ValidationException if the id<=0 or
     *                             firstName is null or does not start with a capital letter or
     *                             lastName is null or does not start with a capital letter or
     *                             email is not a valid email address
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String errors = "";
        if (entity.getId()!=null && entity.getId() <= 0)
            errors += "invalid id!\n";
        String firstName = entity.getFirstName();
        if (firstName == null || firstName.isEmpty() || firstName.charAt(0) < 'A' || firstName.charAt(0) > 'Z')
            errors += "invalid firstName!\n";
        String lastName = entity.getLastName();
        if (lastName == null || lastName.isEmpty() || lastName.charAt(0) < 'A' || lastName.charAt(0) > 'Z')
            errors += "invalid lastName!\n";
        String email= entity.getEmail().trim();
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        boolean b = email.matches(EMAIL_REGEX);
        if(!b)
            errors+="invalid email!\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
