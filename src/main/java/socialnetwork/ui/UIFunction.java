package socialnetwork.ui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class UIFunction {
    private final Method method;
    private final Parameter[] parameters;
    private final UIMethod methodSpecification;
    private final List<String> parametersName;
    public UIFunction(Method method, UIMethod attribute)
    {
        methodSpecification=attribute;
        this.method=method;
        parameters=method.getParameters();
        parametersName = new ArrayList<>();
        for(var parameter: parameters)
            parametersName.add(parameter.getAnnotation(UIParameter.class).value());
    }

    public List<String> getParametersName()
    {
        return parametersName;
    }

    public String getName()
    {
        return methodSpecification.name();
    }

    public String getDescription()
    {
        return methodSpecification.description();
    }

    public void Call(Ui ui, String[] argsString) throws Throwable
    {
        Object[] args = new Object[parameters.length];
        if(argsString.length == 0 && parameters.length > 0)
            argsString = readArguments(ui);

        for(int i=0;i<args.length;i++)
        {
            String arg=argsString[i];
            var type = parameters[i].getType();
            args[i]=convertStringToType(arg, type);
        }

        try {
            method.invoke(ui, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private String[] readArguments(Ui ui) throws IOException
    {
        String[] argsString = new String[parameters.length];
        for(int i=0;i<parameters.length;i++)
            argsString[i] = ui.readString(parametersName.get(i)+"=");

        return argsString;
    }

    private Object convertStringToType(String arg, Class<?> type)
    {
        if (String.class.equals(type))
            return arg;
        if(Integer.class.equals(type))
            return Integer.parseInt(arg);
        if(Long.class.equals(type))
            return Long.parseLong(arg);

        throw new TypeNotFoundException(type, method);
    }

}
