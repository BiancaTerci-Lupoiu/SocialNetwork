package project.lab6;

import javafx.stage.Stage;

public class InfoBuilder {

    private final Info info;
    private InfoBuilder() {
        info = new Info();
    }

    public static InfoBuilder create() {
        return new InfoBuilder();
    }

    public InfoBuilder addIdChat(Long id)
    {
        info.setIdChat(id);
        return this;
    }

    public InfoBuilder addStage(Stage stage)
    {
        info.setStage(stage);
        return this;
    }

    public Info build()
    {
        return info;
    }
}
