package socialnetwork.ui.uiexception;


import java.lang.reflect.Method;

public class ParameterException extends RuntimeException {
    public ParameterException(Method method, String message) {
        super("In functia " + method.getName() + " a aparut eroarea:\n" + message);
    }
}
