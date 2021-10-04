package deco.combatevolved.exceptions;

public class InvalidCoordinatesException extends WorldGenException {
    public InvalidCoordinatesException() {
        super();
    }

    public InvalidCoordinatesException(String message) {
        super(message);
    }

    public InvalidCoordinatesException(String message, Throwable cause) {
        super(message, cause);
    }
}
