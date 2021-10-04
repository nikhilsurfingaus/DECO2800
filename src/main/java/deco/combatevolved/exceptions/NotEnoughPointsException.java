package deco.combatevolved.exceptions;

/**
 * Exception thrown by the Delaunay triangulator when it is initialized with
 * less than three points.
 * Adapted from https://github.com/jdiemke/delaunay-triangulator/blob/master/library/src/main/java/io/github/jdiemke/triangulation/NotEnoughPointsException.java
 *
 * @author Johannes Diemke
 */
public class NotEnoughPointsException extends RuntimeException {

    private static final long serialVersionUID = 7061712854155625067L;

    public NotEnoughPointsException() { super(); }

    public NotEnoughPointsException(String message) {
        super(message);
    }

    public NotEnoughPointsException(String message, Throwable cause) {
        super(message, cause);
    }
}
