package org.example.snakegame;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the snake in the game.
 * It consists of a list of segments and has a direction in which it moves.
 */
public class Snake
{
    private Direction direction = Direction.RIGHT;
    private final List<Segment> segments = new ArrayList<>();

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


    //Getter and setter region

    /**
     * Gets the current direction of the snake
     * @return the direction the snake is moving in.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Changes the snake's direction, if the new direction is not opposite of current direction
     * @param newDirection The direction to turn to.
     */
    public void setDirection(Direction newDirection)
    {
        if ((this.direction == Direction.UP && newDirection != Direction.DOWN) ||
                (this.direction == Direction.DOWN && newDirection != Direction.UP) ||
                (this.direction == Direction.LEFT && newDirection != Direction.RIGHT) ||
                (this.direction == Direction.RIGHT && newDirection != Direction.LEFT))
        {
            this.direction = newDirection;
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

        //Bør anvendes i Game-klassen i render metoden
    }
}
