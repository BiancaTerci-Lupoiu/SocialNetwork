package project.lab6.repository.repointerface;

import project.lab6.domain.Tuple;
import project.lab6.domain.TupleWithIdChatUser;
import project.lab6.domain.chat.UserChatInfo;

import java.util.List;

public interface RepositoryUserChatInfo extends Repository<TupleWithIdChatUser, UserChatInfo> {
    List<UserChatInfo> getUserChatInfoFromChat(Long chatId);
}
