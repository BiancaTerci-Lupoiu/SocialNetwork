package project.lab6.domain.validators;

import project.lab6.domain.chat.Message;

public class MessageValidator implements Validator<Message> {
    /**
     * verify if a message is valid
     *
     * @param entity - the entity to be validated (Message)
     * @throws ValidationException if the text is empty or null or
     *                             date is null or
     *                             idUserFrom is null or
     *                             idChat is null
     */
    @Override
    public void validate(Message entity) throws ValidationException {
        String errors = "";
        if (entity.getText().trim().isEmpty() || entity.getText() == null)
            errors += "invalid text!\n";
        if (entity.getDate() == null)
            errors += "invalid date!\n";
        if (entity.getIdUserFrom() == null)
            errors += "invalid idUserFrom!\n";
        if (entity.getIdChat() == null)
            errors += "invalid idChat!\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
