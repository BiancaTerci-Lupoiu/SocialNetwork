package socialnetwork.ui.uiexception;
import java.lang.reflect.Method;

/**
 * Exception that will be thrown when there is a problem with a parameter
 */
public class ParameterException extends RuntimeException {
    public ParameterException(Method method, String message) {
        super("In functia " + method.getName() + " a aparut eroarea:\n" + message);
    }
}
