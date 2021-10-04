package deco.combatevolved.exceptions;

public class IllegalWorldSizeException extends WorldException {
    public IllegalWorldSizeException() {
        super();
    }

    public IllegalWorldSizeException(String message) {
        super(message);
    }

    public IllegalWorldSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
