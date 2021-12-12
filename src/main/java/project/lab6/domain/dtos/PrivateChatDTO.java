package project.lab6.domain.dtos;

import javafx.scene.paint.Color;

import java.util.List;

public class PrivateChatDTO extends ChatDTO{
    public PrivateChatDTO(Color color) {
        super(color, true);
    }

    @Override
    public String getName(Long idLoggedUser) {
        List<UserChatInfoDTO> info = getUsersInfo();
        int yoursIndex;
        for(yoursIndex=0;yoursIndex<2;yoursIndex++)
            if(info.get(yoursIndex).getUser().getId().equals(idLoggedUser))
                break;
        return info.get(yoursIndex^1).getNickname();
    }
}