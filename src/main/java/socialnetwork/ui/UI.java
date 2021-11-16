package socialnetwork.ui;

import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.Service;
import socialnetwork.service.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public abstract class UI {
    private final Map<String, UIFunction> commands;
    private final Map<String, UIFunction> shortCommands;
    private final BufferedReader inputReader;
    protected UI() {
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

    /**
     * starts the application
     */
    public void start() {
        while (true) {
            try {
                System.out.print(">>>");
                String commandLine = inputReader.readLine();
                String[] args = commandLine.split(" ");
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
            }catch (DateTimeParseException | ServiceException | ValidationException ex)
            {
                System.out.println(ex.getMessage());
            } catch (ExitException ex)
            {
                break;
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @UIMethod(name = "help", description = "Shows this menu")
    public void help() {
        for (var command : commands.values()) {
            System.out.println(command.getShortName() + ") " + command.getName() + " " + String.join(" ", command.getParametersName().stream().map(x -> "<" + x + ">").toList()) + " -> " + command.getDescription());
        }
    }
}
