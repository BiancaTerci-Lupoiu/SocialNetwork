package project.lab6.ui.console.uiexception;
import java.lang.reflect.Method;

/**
 * Exception that will be thrown when the class of a parameter from UI can't be read
 */
public class TypeNotFoundException extends RuntimeException{
    public TypeNotFoundException(Class<?> type, Method method) {
        super("Nu stiu sa tratez argumentul de tipul "+type.getName()+
        " al functiei "+method.getName());
    }

}
