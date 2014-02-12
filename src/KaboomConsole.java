import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Random;


public class KaboomConsole {
	private int boardNum, boardPrefSize, boardDifficulty;
	private Preferences prefs;
	private Reader reader;
	private Writer writer;
	private static final int kNumBoards = 5000;
	
    /** Entry point for the application.
    *
    *  @param args ignored     
    */
   public static void main(String[] args)
   {   
       KaboomConsole app = new KaboomConsole(); 
       app.setIOsources(new InputStreamReader(System.in), 
           new OutputStreamWriter(System.out));
       app.run();
   }

   /** Construct a new instance of this class.
    * Create the game components and initialize them,
    * create the user interface and connect it to the game.
    */
   public KaboomConsole()
   {
       try
       {
           this.boardNum = new Random().nextInt(kNumBoards);
           this.prefs = new Preferences();
           this.boardPrefSize = prefs.getBoardSize();
           this.boardDifficulty = prefs.getDifficulty();
       }
       catch(IOException e)
       {
           e.printStackTrace();
       }
   }
   
   /** Set input/output sources for Stream-based user interfaces.
    * @param rdr A Reader from which user input is obtained.
    * @param wtr A Writer to which program output is displayed.
    */
   public void setIOsources(Reader rdr, Writer wtr) 
   {
       this.reader = new BufferedReader(rdr);
       this.writer = new PrintWriter(wtr);
   }
   
   /**
    * Begins a Kaboom Game.
    */
   public void run()
   {
	   
   }
}
