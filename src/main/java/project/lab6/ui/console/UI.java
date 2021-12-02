package project.lab6.ui.console;

import project.lab6.domain.User;
import project.lab6.domain.validators.ValidationException;
import project.lab6.service.Service;
import project.lab6.service.ServiceException;
import project.lab6.ui.console.uiexception.ExitException;
import project.lab6.ui.console.uiexception.IncorectNumberOfParametersException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeParseException;
import java.util.*;

public abstract class UI {
    private final Map<String, UIFunction> commands;
    private final Map<String, UIFunction> shortCommands;
    private final BufferedReader inputReader;
    protected final Service service;

    protected UI(Service service) {
        this.service = service;
        commands = new TreeMap<>();
        shortCommands = new TreeMap<>();
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        Initialize();
    }

    private void Initialize() {
        for (var method : getClass().getMethods()) {
            var attribute = method.getAnnotation(UIMethod.class);
            if (attribute != null) {
                var function = new UIFunction(method, attribute);
                commands.put(function.getName(), function);
            }
        }
        int number = 1;
        for (var command : commands.values()) {
            command.setShortName(Integer.toString(number));
            number += 1;
            shortCommands.put(command.getShortName(), command);
        }
    }

    /**
     * Shows the specified message to the user and reads a line from standard input
     *
     * @param message String The message to be show
     * @return String
     */
    public String readString(String message) throws IOException {
        System.out.print(message);
        return inputReader.readLine();
    }

    private String[] splitArguments(String line) {
        String[] quoteSeparated = line.split("\"", -1);
        List<String> args = new ArrayList<>();
        for (int i = 0; i < quoteSeparated.length; i++) {
            if (quoteSeparated[i].trim().equals(""))
                continue;
            if (i % 2 == 0)
                args.addAll(Arrays.stream(quoteSeparated[i].split(" "))
                        .map(String::trim)
                        .filter(x -> !x.equals(""))
                        .toList());
            else
                args.add(quoteSeparated[i]);
        }
        if (args.size() == 0)
            return new String[]{""};
        return args.toArray(String[]::new);
    }

    /**
     * starts the application
     */
    public void start() {
        while (true) {
            try {
                System.out.print(">>>");
                String commandLine = inputReader.readLine();

                String[] args = splitArguments(commandLine);
                String command = args[0];
                var function = commands.get(command);
                if (function == null)
                    function = shortCommands.get(command);
                if (function == null) {
                    System.out.println("The command is not recognized!");
                    System.out.println("Try the command help!");
                    continue;
                }

                function.Call(this, Arrays.copyOfRange(args, 1, args.length));
            } catch (NumberFormatException ex) {
                System.out.println("Wrong input for number");
            } catch (DateTimeParseException | ServiceException | ValidationException | IncorectNumberOfParametersException ex) {
                System.out.println(ex.getMessage());
            } catch (ExitException ex) {
                break;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                break;
            }
        }
    }

    @UIMethod(name = "help", description = "Shows this menu")
    public void help() {
        for (var command : commands.values()) {
            System.out.println(command.getShortName() + ") " + command.getName() + " " + String.join(" ", command.getParametersName().stream().map(x -> "<" + x + ">").toList()) + " -> " + command.getDescription());
        }
    }

    /**
     * ui function to print the users
     */
    @UIMethod(name = "showUsers", description = "shows all the users")
    public void getAllUsers() {
        for (User user : service.getAllUsers().values())
            System.out.println(user.toStringWithFriends());
    }
}
