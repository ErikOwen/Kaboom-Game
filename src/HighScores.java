import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Class which helps manage the Kaboom Game's high score
 * functionality.
 * 
 * @author erikowen
 * @version 1
 */
public class HighScores
{

    /*Path to high scores directory*/
    private final static String kHallOfFameDirPath = "kaboom";
    /*Path to high scores file*/
    private final static String kHallOfFamePath = "kaboom/halloffame.ser";
    private File highScoresFile;
    /*How many hall of fame entries to return */
    public final static int kHallSize = 5;
    
    /**
     * Constructor to create a new HighScores object
     * @throws IOException 
     */
    public HighScores() throws IOException
    {
        this.highScoresFile = new File(kHallOfFamePath);

        /*Creates a high scores file if it does not exist*/
        if(!highScoresFile.exists())
        {       
            highScoresFile.createNewFile();
        }
    }
    
    /** Adds a new high score to the high scores file
     * 
     * @param name The name of the score with the high score
     * @param time The time in which the player finished the game
     */
    public void addHighScore(String name, int time) throws IOException
    {       
        /*Writes to the high score file*/
        FileWriter highScoreWriter = new FileWriter(highScoresFile, true);
        highScoreWriter.write(time + " " + name + "\n");
        highScoreWriter.close();
    }
    
    /**
     * Helper method to parse and sort the high scores
     * @return a string of the parsed and sorted high scores
     * @throws IOException thrown by scanning from the high scores file
     */
    private String parseAndSortHighScores() throws IOException
    {
        String highScoresString = "";
        ArrayList<HighScore> scoresList = new ArrayList<HighScore>();
        
        Scanner scan = new Scanner(this.highScoresFile);
        
        /*Reads all of the scores in the high scores file*/
        while(scan.hasNextLine())
        {
            String curLine = scan.nextLine();
            scoresList.add(new HighScore(Integer.parseInt(curLine.substring(0,
                 curLine.indexOf(" "))), curLine.substring(curLine.indexOf(" ") + 1,
                      curLine.length())));
        }
        
        scan.close();
        Collections.sort(scoresList, new HighScoreComparator());
        
        /*Gets the top 5 best scores*/
        for(int scoreNdx = 0; scoreNdx < kHallSize && scoreNdx < scoresList.size();
            scoreNdx++)
        {
            HighScore curScore = scoresList.get(scoreNdx);
            highScoresString = highScoresString.concat(curScore.getTimeString() + "  " + curScore.getName() + "\n");
        }
        highScoresString = highScoresString.concat("\n");
        return highScoresString;
    }
    
    /** Return a string representation of the top five high scores. 
     *  @return string is the top scores, one per line, with the
     *  score and name (in that order), separated by one or more blanks.
     *  Name is twenty characters max.  Leading blanks are allowed.
     */
    public String getHighScores()
    {   
    	String highScoresString = "";
        try
        {
            /*Gets the high scores if the file exists*/
            if (this.highScoresFile.exists() && this.highScoresFile.length() > 0)
            {
                highScoresString = parseAndSortHighScores();
            }
        }
        catch(Exception e)
        {
            highScoresString = "";
        }
        
        return highScoresString;
    }
    
    /**
     * Erases the high scores file
     */
    public void eraseHighScores()
    {
        try
        {
            File file = new File("kaboom/halloffame.ser");
            file.delete();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
