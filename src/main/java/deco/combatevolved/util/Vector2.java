package deco.combatevolved.util;

import java.util.Objects;

public class Vector2 {
    private float x;
    private float y;

    /**
     * Constructor for a Vector2
     * @param x the x value for the vector
     * @param y the y value for the vector
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for the x value of the vector
     * @return the x value
     */
    public float getX() {
        return x;
    }

    /**
     * Getter for the y value of the vector
     * @return the y value
     */
    public float getY() {
        return y;
    }

    /**
     * Setter for the x value
     * @param x the x value to be set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Setter for the y value
     * @param y the y value to be set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Equals Method returns true iff the two objects are equal 
     * based on their X and Y Value.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2)) {
            return false;
        }
        Vector2 vector = (Vector2) obj;
        return vector.getX() == this.getX() && vector.getY() == this.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /* ------------------------------------------------------------------------
     * 	    HELPER FUNCTIONS FOR WORLD GENERATION BELOW THIS COMMENT.
     * ------------------------------------------------------------------------ */
    // Code adapted from https://github.com/jdiemke/delaunay-triangulator/blob/master/library/src/main/java/io/github/jdiemke/triangulation/Vector2D.java


    /**
     * Subtracts the given vector from this
     * @param vector The vector to be subtracted from this
     * @return A new instance holding the result of the vector subtraction
     */
    public Vector2 sub(Vector2 vector) {
        return new Vector2((this.x - vector.getX()), (this.y - vector.getY()));
    }

    /**
     * Adds the given vector to this
     * @param vector The vector to be added to this
     * @return A new instance holding the result of the vector addition
     */
    public Vector2 add(Vector2 vector) {
        return new Vector2((this.x + vector.getX()), (this.y + vector.getY()));
    }

    /**
     * Multiplies this by the given scalar.
     *
     * @param scalar
     *            The scalar to be multiplied by this
     * @return A new instance holding the result of the multiplication
     */
    public Vector2 mult(float scalar) {
        return new Vector2((this.x * scalar), (this.y * scalar));
    }

    /**
     * Computes the magnitude or length of this.
     *
     * @return The magnitude of this
     */
    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Computes the dot product of this and the given vector.
     *
     * @param vector
     *            The vector to be multiplied by this
     * @return A new instance holding the result of the multiplication
     */
    public double dot(Vector2 vector) {
        return (this.x * vector.x) + (this.y * vector.y);
    }

    /**
     * Computes the 2D pseudo cross product Dot(Perp(this), vector) of this and
     * the given vector.
     *
     * @param vector
     *            The vector to be multiplied to the perpendicular vector of
     *            this
     * @return A new instance holding the result of the pseudo cross product
     */
    public double cross(Vector2 vector) {
        return (this.y * vector.x) - (this.x * vector.y);
    }

    @Override
    public String toString() {
        return "Vector2[" + x + ", " + y + "]";
    }
}
