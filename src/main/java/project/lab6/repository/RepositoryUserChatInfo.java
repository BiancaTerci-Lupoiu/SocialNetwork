package project.lab6.repository;

import project.lab6.domain.Tuple;
import project.lab6.domain.chat.UserChatInfo;

import java.util.List;

public interface RepositoryUserChatInfo extends Repository<Tuple<Long,Long>, UserChatInfo> {
    List<UserChatInfo> getUserChatInfoFromChat(Long chatId);
}
