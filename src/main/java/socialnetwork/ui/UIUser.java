package socialnetwork.ui;

import socialnetwork.domain.User;
import socialnetwork.service.Service;
import socialnetwork.ui.uiexception.ExitException;

public class UIUser extends UI{
    private final Service service;
    private final User user;

    public UIUser(Service service, User user) {
        this.service = service;
        this.user = user;
    }

    @UIMethod(name = "logout", description = "closes this active session")
    public void logout()
    {
        throw new ExitException();
    }
}
