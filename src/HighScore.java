import javax.swing.Timer;


/**
 * This is an immutable class representing a highscore.
 * 
 * @author Erik Owen
 * @version 1
 */
public class HighScore
{
    /*the highscore: time it took the user to complete the game*/
    private int timeInSeconds;
    private String name;
    private static final int kSecondsInMinute = 60;

    /**
     * Constructor for objects of class HighScore
     * 
     * @param time the the time (in seconds) it took the user
     * to complete the game
     * @param name the name of the player
     */
    public HighScore(int time, String name)
    {
        this.timeInSeconds = time;
        this.name = name;
    }

    /**
     * Accessor method for getting a highscore, in
     * the correct String format.
     * 
     * @return  the time it took to get this high score,
     * in the correct String representation.
     */
    public String getTimeString()
    {
    	int minutes = this.timeInSeconds / kSecondsInMinute;
    	int seconds = this.timeInSeconds % kSecondsInMinute;
    	
    	return minutes + ":" + String.format("%02d", seconds);
    }
    
    /**
     * Accessor method to get the time in seconds of this 
     * high score.
     * 
     * @return int the time in seconds of the high score
     */
    public int getTime() {
    	return this.timeInSeconds;
    }
    
    /**
     * Accessor method for getting the name associated with a highscore.
     * 
     * @return the name of this highscore
     */
    public String getName()
    {
        return this.name;
    }
}
