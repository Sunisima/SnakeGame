package org.example.snakegame;

/**Represents one segment(circle) of the snake
 * A segment has a specific position defined by x and y coordinates
 */
public class Segment
{
    private int x;
    private int y;

    /**
     * Constructs a segment with the given coordinates
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Segment(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the segment
     * @return the x grid position
     */
    public int getX() {
        return x;
    }
    /**
     * Returns the y-coordinate of the segment
     * @return the y grid position
     */
    public int getY() {
        return y;
    }

    /**
     * Checks if this segment is equal to another object.
     * Two segments are considered equal if they have the same x and y coordinates.
     *
     * @param obj the object to compare to
     * @return true if both objects are Segment instances with the same coordinates, otherwise false
     */
    @Override
    public boolean equals(Object obj)
    {
        //Same object in memory then == equal
        if (this == obj) return true;

        //Is the object not a Segment then != equal
        if(!(obj instanceof Segment)) return false;

        //Cast and compare coordinates
        Segment other = (Segment)obj;
        return this.x == other.x && this.y == other.y;
    }
}
