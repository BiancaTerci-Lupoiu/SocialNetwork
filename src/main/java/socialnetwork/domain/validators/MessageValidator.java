package socialnetwork.domain.validators;

import socialnetwork.domain.Message;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageValidator implements Validator<Message> {
    /**
     * verify if a message is valid
     *
     * @param entity - the entity to be validated (Message)
     * @throws ValidationException if the text is empty or null or
     *                             date is null or
     *                             idUserFrom is null or
     *                             idUsersTo is an empty list or contains duplicates
     */
    @Override
    public void validate(Message entity) throws ValidationException {
        String errors = "";
        if (entity.getText().isEmpty() || entity.getText() == null)
            errors += "invalid text!\n";
        if (entity.getDate() == null)
            errors += "invalid date!\n";
        if (entity.getIdUserFrom() == null)
            errors += "invalid idUserFrom!\n";
        List<Long> listUsersTo = entity.getIdUsersTo();
        if (listUsersTo.isEmpty())
            errors += "no recipient provided!\n";
        Set<Long> listUsersToWithoutDuplicates = new HashSet<>(listUsersTo);
        if (listUsersTo.size() != listUsersToWithoutDuplicates.size())
            errors += "duplicates are not allowed in the recipients list!\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);


    }
}
