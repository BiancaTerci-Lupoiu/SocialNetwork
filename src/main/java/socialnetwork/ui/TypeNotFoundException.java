package socialnetwork.ui;

import java.lang.reflect.Method;

public class TypeNotFoundException extends RuntimeException{

    public TypeNotFoundException(Class<?> type, Method method) {
        super("Nu stiu sa tratez argumentul de tipul "+type.getName()+
        " al functiei "+method.getName());
    }

}
