package org.example.snakegame;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the snake in the game.
 * It consists of a list of segments and has a direction in which it moves.
 */
public class Snake
{
    private boolean headEnlarged = false; // Keep track of whether the head is enlarged.
    private double speed = 300; // default speed in ms
    private Direction direction = Direction.RIGHT;
    private final List<Segment> segments = new ArrayList<>();
    private long lastDirectionChangeTime = 0;
    private static final long TURN_DELAY = 150; // In milliseconds

    /**
     * Constructs a new Snake object.
     * Initializes the snake with a starting direction and one segment.
     */
    public Snake()
    {
        //Snakes starting position
        this.segments.add(new Segment(5,5));
    }

    /**
     * Moves the snake one step forward in the current direction.
     * Adds a new head and removes the tail (unless the snake is growing).
     */
    public void move()
    {
        Segment head = segments.get(0);
        int x = head.getX();
        int y = head.getY();

        switch(direction) {
            case UP: y --; break;
            case DOWN: y ++; break;
            case LEFT: x --; break;
            case RIGHT: x ++; break;
        }

        //Add a new head segment at the new position
        segments.add(0, new Segment(x, y));

        //Remove the last segment to maintain current length
        segments.remove(segments.size() - 1);
    }

    /**
     * Increases the length of the snake by adding a new segment at the tail
     * This should be called when the snake eats food.
     */
    public void grow()
    {
        segments.add(new Segment(segments.get(segments.size() - 1).getX(),
                segments.get(segments.size() - 1).getY()));
    }

    /**
     * Checks if the snake has collided with itself
     * @return true if the snake's head touches any other segment, otherwise return false
     */
    public boolean checkCollision()
    {
        Segment head = segments.get(0);
        return segments.subList(1, segments.size()).contains(head);
    }

    /**
     * Checks if the head is touching the edge of the pane
     * @param width
     * @param height
     * @return
     */
    public boolean checkEdgeCollision(int width, int height) {
        Segment head = segments.get(0);
        return head.getX() < 0 || head.getX() >= width || head.getY() < 0 || head.getY() >= height;
    }

    /**
     * Visually enlarges the snake's head by adding an extra segment at the front.
     * This is only a temporary effect and should be reset after a few seconds.
     */
    public void enlargeHead() {
        this.headEnlarged = true;
    }

    /**
     * Resets the snake's head size to normal by removing the extra front segment when the enlargeHead() is used.
     * Removes the additional segment if present.
     */
    public void resetHeadSize() {
        this.headEnlarged = false;
    }

    public boolean isHeadEnlarged() {
        return headEnlarged;
    }

    //region Getter and setter

    /**
     * Gets the current direction of the snake
     * @return the direction the snake is moving in.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Changes the snake's direction, if the new direction is not opposite
     * of current direction on a delay we have predetermined
     * @param newDirection The direction to turn to.
     */
    public void setDirection(Direction newDirection) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDirectionChangeTime >= TURN_DELAY) {
            if ((this.direction == Direction.UP && newDirection != Direction.DOWN) ||
                    (this.direction == Direction.DOWN && newDirection != Direction.UP) ||
                    (this.direction == Direction.LEFT && newDirection != Direction.RIGHT) ||
                    (this.direction == Direction.RIGHT && newDirection != Direction.LEFT)) {
                this.direction = newDirection;
                lastDirectionChangeTime = currentTime;
            }
        }
    }

    /**
     * Gets the current list of snake segments
     * @return A list of segments representing the snakes body
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Returns the current length of the snake
     * @return the number of segments in the snake
     */
    public int getLength()
    {
        return segments.size();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double newSpeed) {
        this.speed = newSpeed;
    }
//endregion

}
