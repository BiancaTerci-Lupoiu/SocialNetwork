package project.lab6.ui.console.uiexception;

/**
 * Exception that will be thrown when an incorrect number of parameters is given in the UI
 */
public class IncorectNumberOfParametersException extends RuntimeException{
    public  IncorectNumberOfParametersException(int expected, int actual)
    {
        super("Expected "+expected+" parameters, but got "+actual+" parameters");
    }
}
