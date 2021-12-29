package project.lab6;

import javafx.stage.Stage;

public class Info {
    private Long idChat=null;
    private Stage stage=null;

    private void throwIfNull(Object object, String objectName) {
        if (object == null)
            throw new RuntimeException("Can't acces information " + objectName + " because it was not provided");
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
