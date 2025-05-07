package org.example.snakegame;

/**
 * Class to control the score
 */

public class Score
{
    // Variable to store the score
    private int value;

    /**
     * Constructor
     */
    public Score()
    {
        this.value = 0;
    }

    /**
     * Updates score for when a food object is eaten
     * @param points
     */
    public void updateScore(int points)
    {
        this.value += points;
    }

    /**
     * Gets current score
     * @return
     */
    public int getScore()
    {
        return this.value;
    }

    /**
     * Resets score to 0
     */
    public void resetScore()
    {
        this.value = 0;
    }

    /**
     * Displays current score
     * @return
     */
    public String displayScore()
    {
        return "Score: " + this.value;
    }
}
