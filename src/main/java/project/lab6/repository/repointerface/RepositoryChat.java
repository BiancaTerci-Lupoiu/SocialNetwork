package project.lab6.repository.repointerface;

import project.lab6.domain.entities.chat.Chat;

public interface RepositoryChat extends Repository<Long, Chat> {
    /**
     * Gets the private chat between 2 users
     *
     * @param idUser1 Long
     * @param idUser2 Long
     * @return The chat between 2 users or null if it doesn't exist
     */
    Chat getPrivateChatBetweenUsers(Long idUser1, Long idUser2);
}
