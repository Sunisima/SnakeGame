package org.example.snakegame;

public class Score
{
    // Variable to store the score
    private int value;

    // Constructor
    public Score()
    {
        this.value = 0;
    }

    // Update the score when the snake eats a food
    public void updateScore(int points)
    {
        this.value += points;
    }

    // Get current score value
    public int getScore()
    {
        return this.value;
    }

    // Resets the score to 0
    public void resetScore()
    {
        this.value = 0;
    }

    // Displays score
    public String displayScore()
    {
        return "Score: " + this.value;
    }
}
