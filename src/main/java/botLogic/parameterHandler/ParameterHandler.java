package botLogic.parameterHandler;

public interface ParameterHandler {
    String startMessage();
    void handle(String message) throws RuntimeException;
}
