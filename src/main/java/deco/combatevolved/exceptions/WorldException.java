package deco.combatevolved.exceptions;

/**
 * An exception thrown when trying to generate a world
 */
public class WorldException extends Exception {
    public WorldException() {
        super();
    }

    public WorldException(String message) {
        super(message);
    }

    public WorldException(String message, Throwable cause) {
        super(message, cause);
    }
}
