package project.lab6.domain.validators;

import project.lab6.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    /**
     * verify if a friendship is valid
     *
     * @param entity - the entity to be validated (Friendship)
     * @throws ValidationException if the ids of the friendships are null or <=0 or
     *                             if the ids of the friendship are equal
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errors = "";
        if (entity.getId().getLeft() == null || entity.getId().getRight() == null || entity.getId().getLeft() <= 0 || entity.getId().getRight() <= 0)
            errors += "invalid ids at friendship!\n";
        if (entity.getId().getLeft().equals(entity.getId().getRight()))
            errors += "ids are equal!\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
