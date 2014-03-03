import java.awt.*;
import java.io.*;
import java.util.Scanner;
/**
 * Have PlaybackRobot run KaboomGUI then check the high scores list.
 * Assumes board size 8 is set in preferences.ini.
 *
 * @author  jdalbey
 * @version 2014.2.9
 */
public class KaboomGUITest extends junit.framework.TestCase
{
    private KaboomGUI gui;
    /**
     * Default constructor for test class KaboomGUITest
     */
    public KaboomGUITest()
    {
        // Start the application
        gui = new KaboomGUI();
        gui.run();
    }
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    protected void setUp()    
    {
        // Delete any hall of fame file before each test.
        try{
            File file = new File("kaboom"+System.getProperty("file.separator")+"halloffame.ser");
            file.delete();
        }catch(Exception e){
            e.printStackTrace();
        }        
    }
    /* Private method that runs the robot */
    private void runTest(String input, String expected, String errMsg) 
    {
        //System.out.println("Test for : " + errMsg);
        Scanner scan = new Scanner(new StringReader(input));
        PlaybackRobot robot = new PlaybackRobot(scan);
        //robot.setLogging(true);
        robot.runScript();
        String scores = gui.getHighScores();
        // System.out.println(scores);
        // remove blanks and newlines for comparision
        scores = scores.replaceAll(" ","").replaceAll("\\n","");
        boolean passed = scores.indexOf(expected) >= 0;
        assertTrue(errMsg, passed);
    }

    public void testGUIplay()
    {
        String script = "press_key alt\n type_key r\n release_key alt\n  wait 100\n";
        String expected = "";
        String errMsg = "Failed launching and restarting";
        runTest(script, expected, errMsg);
        expected = "Snoopy";
        errMsg = "Failed cheating";
        runTest(cheatwin, expected, errMsg);
        runTest(goHome + cheatlose, "Snoopy", "Failed losing after cheat");
        expected = "Linus";
        errMsg = "Failed winning board 7";
        runTest(startBrd7+winBoard7, expected, errMsg);
        runTest(goHome + winBoard105, "Patty", "Failed playing board 105");
        runTest(prefsSize, "Patty", "Failed prefs board size");
        runTest(about + quitGame, "Patty", "Failed about box");
    }

    public void testFlagging()
    {
        String script = "press_key alt\n type_key r\n release_key alt\n  wait 500\n"
            + "mouse_rightpress 50,150\n mouse_rightpress 100,175\n mouse_rightpress 150,220\n "
            + "wait 1000\n"
            + "mouse_rightpress 50,150\n mouse_rightpress 100,175\n mouse_rightpress 150,220\n "
            + "wait 1000\n";
        String expected = "";
        String errMsg = "Failed launching and restarting";
        runTest(script + quitGame, expected, errMsg);
    }

    public void testTimer()
    {
        String script = startBrd7 + "wait 2100\n " + winBoard7 + goHome + winBoard105 + quitGame;
        Scanner scan = new Scanner(new StringReader(script));
        PlaybackRobot robot = new PlaybackRobot(scan);
        robot.runScript();
        String scores = gui.getHighScores();
        // remove blanks and newlines for comparision
        scores = scores.replaceAll(" ","").replaceAll("\\n","");
        assertEquals("Timer test failed", "0:00Patty0:03Linus", scores);
    }
    
    String newGame = 
          " press_key alt\n type_key g\n release_key alt\n  wait 200\n"         // Select Game 5000
        + " type_string 5000\n type_key enter\n  wait 200\n"
        +  " press_key alt\n type_key n\n release_key alt\n  wait 200\n"         // New game: 1
        +  " press_key alt\n type_key n\n release_key alt\n  wait 200\n";         // New game: 2
        
    String winBoard105 = 
//        a1a7a8e8f6g6g4
          " press_key alt\n type_key g\n release_key alt\n  wait 200\n"         // Select Game 105
        + " type_string 105\n type_key enter\n  wait 250\n"
        + " type_key space \n" // A1 
        + " type_key right\n type_key right\n type_key right\n type_key right\n"
        + " type_key right\n type_key right\n " 
        + " type_key space\n"   // A7
        + " type_key right\n  type_key space\n" //A8
        + " type_key down\n  type_key down\n type_key down\n type_key down\n"
        + " type_key space\n"   // E8
        + " type_key left\n  type_key left\n  type_key down\n"
        + " type_key space\n"   // F6
        + " type_key down\n"
        + " type_key space\n"   // G6        
        + " type_key left\n  type_key left\n  "        
        + " type_key space\n wait 200\n"   // G4
        + " type_key y \n type_key enter\n wait 250\n"
        + " type_string Patty\n type_key enter\n  wait 500\n";
        
    String goHome = 
         " type_key left\n type_key left\n type_key left\n type_key left\n "        // return cursor to upper left
        + " type_key up\n type_key up\n type_key up\n type_key up\n type_key up\n type_key up\n type_key up\n"
        + " wait 100 \n";

    String startBrd7 =     
          " press_key alt\n type_key g\n release_key alt\n  wait 200\n"         // Select Game 7
        + " type_string 7\n type_key enter\n  wait 250\n";
    String winBoard7 = 
          " type_key right\n type_key right\n type_key right\n type_key right\n"
        + " type_key right\n type_key right\n type_key right\n type_key right\n" 
        + " type_key space\n"   // A8
        + " type_key down\n type_key down\n type_key down\n type_key down\n"
        + " type_key down\n type_key down\n type_key down\n"        
        + " type_key space\n"   // H8
        + " type_key left\n type_key left\n type_key left\n type_key left\n "
        + " type_key space\n  wait 250 \n "   // H4
        + " type_key y \n type_key enter\n  wait 250\n"
        + " type_string Linus\n wait 200 \n type_key enter\n  wait 500\n";
        
    String showHall = 
           // Show hall of fame dialog for three seconds
          " press_key alt\n type_key s\n release_key alt\n  wait 3000\n type_key space\n  wait 100\n";

    String cheatwin = " press_key alt\n type_key c\n release_key alt\n"
        + "type_key tab\n type_key right\n type_key space\n wait 250\n"
        + " type_key y \n wait 500\n type_key enter\n wait 500\n"
        + " type_string Snoopy\n type_key enter\n  wait 500\n";
        
    String cheatlose =  " press_key alt\n type_key r\n release_key alt\n "
        + " press_key alt\n type_key c\n release_key alt\n"
        + " type_key space\n wait 500\n"
        + " type_key enter\n wait 100\n";

    String prefsSize =
         " press_key alt\n type_key g\n release_key alt\n  wait 200\n"         // Select Game 16
       + " type_string 16\n type_key enter\n  wait 250\n"
       + " press_key alt\n type_key f\n release_key alt\n  wait 200\n"
       + " type_key tab\ntype_key tab\n type_key space\n wait 500\n"
       + " type_key tab\n wait 100\n type_key tab\n wait 100\n type_key tab\n wait 100\n type_key tab\n wait 200\n type_key space\n wait 200\n"
       + " press_key alt\n type_key p\n release_key alt\n ";
       
    String about =  
          " press_key alt\n type_key a\n release_key alt\n  wait 2000\n"  
        + " type_key enter\n  wait 250\n";
        
    String quitGame = " press_key alt\n type_key q\n release_key alt\n  wait 100\n";

}