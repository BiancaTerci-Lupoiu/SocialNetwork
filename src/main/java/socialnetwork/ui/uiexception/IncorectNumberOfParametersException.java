package socialnetwork.ui.uiexception;

public class IncorectNumberOfParametersException extends RuntimeException{
    public  IncorectNumberOfParametersException(int expected, int actual)
    {
        super("Expected "+expected+" parameters, but got "+actual+" parameters");
    }
}
