package deco.combatevolved.exceptions;

/**
 * An exception thrown when trying to find the circuncentre of three
 * collinear points
 */
public class CollinearPointsException extends WorldGenException {
    public  CollinearPointsException() {super();}

    public CollinearPointsException(String message) {super(message);}

    public CollinearPointsException(String message, Throwable cause) {
        super(message, cause);
    }
}
