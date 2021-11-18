package socialnetwork.ui;

import socialnetwork.service.ServiceException;
import socialnetwork.ui.uiexception.IncorectNumberOfParametersException;
import socialnetwork.ui.uiexception.ParameterException;
import socialnetwork.ui.uiexception.TypeNotFoundException;
import socialnetwork.utils.Constants;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UIFunction {
    private final Method method;
    private final Parameter[] parameters;
    private final UIMethod methodSpecification;
    private final List<String> parametersName;
    private String shortName;
    private final boolean hasListParameter;

    public UIFunction(Method method, UIMethod attribute) {
        methodSpecification = attribute;
        this.method = method;
        parameters = method.getParameters();
        parametersName = new ArrayList<>();
        for (var parameter : parameters)
        {
            var parameterAnnotation = parameter.getAnnotation(UIParameter.class);
            if(parameterAnnotation == null)
                throw new ParameterException(method, "Nu a fost specificat @UIParameter la unul dintre argumente");
            parametersName.add(parameterAnnotation.value());
        }

        for (int i = 0; i < parameters.length - 1; i++)
            if (parameters[i].getType().equals(List.class))
                throw new ParameterException(method, "Doar ultimul argument poate avea tipul lista!");
        hasListParameter = parameters.length > 0 && parameters[parameters.length - 1].getType().equals(List.class);
    }

    public List<String> getParametersName() {
        return parametersName;
    }

    public String getName() {
        return methodSpecification.name();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String name) {
        shortName = name;
    }

    public String getDescription() {
        return methodSpecification.description();
    }

    /**
     * Calls this method on the specific UI
     * @param ui the UI where this method belongs
     * @param argsString the string of arguments that are given in line.
     *                   If no argument is given, there are read in this function
     * @throws Throwable throws the Exception of the underlying method
     */
    public void Call(UI ui, String[] argsString) throws Throwable {
        Object[] args = new Object[parameters.length];
        if (argsString.length == 0 && parameters.length > 0)
            argsString = readArguments(ui);
        if (argsString.length < parameters.length ||
                argsString.length > parameters.length && !hasListParameter)
            throw new IncorectNumberOfParametersException(parameters.length, argsString.length);

        int j = 0;
        for (int i = 0; i < args.length; i++) {
            var type = parameters[i].getType();
            var c = parameters[i].getParameterizedType();
            if (List.class.equals(type)) {
                Class<?> genericParameter = (Class<?>) ((ParameterizedType) parameters[i].getParameterizedType()).getActualTypeArguments()[0];
                List<Object> list = new ArrayList<>();
                for (; j < argsString.length; j++) {
                    var arg = convertStringToType(argsString[j], genericParameter);
                    list.add(arg);
                }
                args[i] = list;
            } else
                args[i] = convertStringToType(argsString[j++], type);
        }

        try {
            method.invoke(ui, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    /**
     * Reads the arguments necessary for the underlying method
     * @param ui the UI where this method belongs
     * @return the array of String read from the UI
     */
    private String[] readArguments(UI ui) throws IOException {
        List<String> argsString = new ArrayList<>(parameters.length);
        int i;
        for (i = 0; i < parameters.length - 1; i++)
            argsString.add(ui.readString(parametersName.get(i) + "="));

        if (hasListParameter) {
            String parameterName = parametersName.get(i);
            int j = 0;
            while (true) {
                String line = ui.readString(parameterName + "[" + j + "]=");
                if (line.trim().equals(""))
                    break;
                argsString.add(line);
                j++;
            }
        } else
            argsString.add(ui.readString(parametersName.get(i) + "="));
        return argsString.toArray(new String[0]);
    }

    /**
     * Converts the arg to the specified type
     * @param arg The argument to convert
     * @param type The resulting class of the conversion
     * @return The converted Object
     */
    private Object convertStringToType(String arg, Class<?> type) {
        if (String.class.equals(type))
            return arg;
        if (Integer.class.equals(type))
            return Integer.parseInt(arg);
        if (Long.class.equals(type))
            return Long.parseLong(arg);
        if (LocalDateTime.class.equals(type))
            return LocalDateTime.parse(arg, Constants.DATETIME_FORMATTER);
        if (LocalDate.class.equals(type))
            return LocalDate.parse(arg, Constants.DATE_FORMATTER);
        throw new TypeNotFoundException(type, method);
    }

}
