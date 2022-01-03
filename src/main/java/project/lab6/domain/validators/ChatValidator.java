package project.lab6.domain.validators;

import project.lab6.domain.chat.Chat;

public class ChatValidator implements Validator<Chat> {
    /**
     * verify if a chat is valid
     *
     * @param chat - the entity to be validated (Chat)
     * @throws ValidationException if the chat is not private and its name is empty (null or "")
     */
    @Override
    public void validate(Chat chat) throws ValidationException {
        String errors = "";
        if (!chat.isPrivateChat() && (chat.getName() == null || chat.getName().trim().equals("")))
            errors = "The name of the chat cannot be empty!\n";
        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
