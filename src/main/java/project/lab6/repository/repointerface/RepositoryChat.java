package project.lab6.repository.repointerface;

import project.lab6.domain.chat.Chat;

public interface RepositoryChat extends Repository<Long, Chat> {
    /**
     * Gets the private chat between 2 users
     * @param idUser1 Long
     * @param idUser2 Long
     * @return The chat between 2 users or null if it doesn't exist
     */
    Chat getPrivateChatBetweenUsers(Long idUser1, Long idUser2);

    /**
     * Saves the chat in the repo
     * @param chat the chat to be saves
     * @return the chat that has been saved with the id or null if the operation ended with error
     */
    Chat saveAndReturnChat(Chat chat);
}
