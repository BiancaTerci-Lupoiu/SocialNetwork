package project.lab6.domain.validators;

import project.lab6.domain.chat.UserChatInfo;

public class UserChatInfoValidator implements Validator<UserChatInfo> {
    /**
     * verify if a chatInfo is valid
     *
     * @param entity - the entity to be validated (ChatInfo)
     * @throws ValidationException if the idChat is null or
     *                             idUser is null or
     *                             nickname is null or empty
     */
    @Override
    public void validate(UserChatInfo entity) throws ValidationException {
        String errors = "";
        if (entity.getIdChat() == null)
            errors += "invalid idChat\n";
        if (entity.getIdUser() == null)
            errors += "invalid idUser";
        if (entity.getNickname() == null || entity.getNickname().trim().isEmpty())
            errors += "invalid nickname";
        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
