import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Integer;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import java.util.Map;


/**
 * Class which gives access to the Collapse game preferences, like
 * the board size.
 * 
 * @author erikowen
 * @version 1
 */
public class Preferences
{
    private int boardSize, difficulty;
    private Ini ini;
    private Ini.Section boardSection, difficultySection;
    private final String kPreferencesPath = "kaboom/preferences.ini";
    
    /**
     * Constructor to make a Preferences object
     */
    public Preferences() throws IOException
    {
        ini = new Ini();
        ini.load(new FileReader(new File(kPreferencesPath)));
        this.boardSection = (Ini.Section)ini.get("Board Size");
//        this.boardSize = Integer.parseInt((String)boardSection.get("small"));
        
        this.difficultySection = (Ini.Section)ini.get("Difficulty");
//      this.difficulty = Integer.parseInt((String)difficultySection.get("easy"));
    }
    
    /**
     * Accessor method for the preferred board size.
     * 
     * @return int representing the desired board size.
     */
    public int getDefaultBoardSize()
    {
        return Integer.parseInt((String)boardSection.get("small"));
    }
    
    /**
     * Accessor method to get the different board sizes in preferences
     * 
     * @return Ini.Section of the different board sizes
     */
    public Ini.Section getBoardSizes()
    {
        return this.boardSection;
    }
    
    /**
     * Accessor method for the preferred difficulty.
     * 
     * @return int representing the number of bombs
     * to be placed on the board.
     */
    public int getDefaultDifficulty()
    {
        return Integer.parseInt((String)difficultySection.get("easy"));
    }
    
    /**
     * Get the difficulties
     * 
     * @return an Ini.Section of the difficulties in the preferences file
     */
    public Ini.Section getDifficulties()
    {
        return this.difficultySection;
    }
}
