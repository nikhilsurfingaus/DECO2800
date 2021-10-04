package deco.combatevolved.exceptions;

/**
 * An exception thrown when trying to generate a world
 */
public class WorldGenException extends Exception {
    public WorldGenException() {
        super();
    }

    public WorldGenException(String message) {
        super(message);
    }

    public WorldGenException(String message, Throwable cause) {
        super(message, cause);
    }
}
