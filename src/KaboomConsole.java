import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Random;
import java.util.Scanner;


public class KaboomConsole {
	private int boardNum, boardPrefSize, boardDifficulty;
	private Preferences prefs;
	private HighScores hallOfFame;
	private Reader reader;
	private Writer writer;
	private KaboomGame game;
	private static final int kNumBoards = 5000, kRestart = 1, kNewGame = 2,
		kSelectGame = 3, kHighScores = 4, kPeek = 5, kCheat = 6, kAbout = 7,
		kQuit = 8, kPreferences = 9, kNumOptions = 9, kSmallBoard = 8,
		kMediumBoard = 10, kLargeBoard = 12, kEasyDifficulty = 8, kModerateDifficulty = 6,
		kHardDifficulty = 4;
	
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
           this.hallOfFame = new HighScores();
           this.boardPrefSize = prefs.getBoardSize();
           this.boardDifficulty = prefs.getDifficulty();
           this.game = new KaboomGame(this.boardPrefSize, this.boardDifficulty, this.boardNum);
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
	   String userInput = "";
	   int userChoice = 0;
	   Scanner scan = new Scanner(this.reader);
	   
	   while(userChoice != kQuit && scan.hasNext())
	   {
		   userInput = scan.nextLine().trim();
		   try
		   {
			   if(userInput.length() == 1)
			   {
				   userChoice = Integer.parseInt(userInput);
				   if(userChoice >= 1 && userChoice <= kNumOptions)
				   {
					   executeOption(userChoice, scan);
				   }
			   }
			   else
			   {
				   makeMove(userInput);
			   }
		   }
		   catch(Exception e)
		   {
			   //could be a number format exception
		   }
	   }
   }
   
   private void executeOption(int optionNumber, Scanner scan) throws IOException
   {
	   switch(optionNumber)
	   {
	   case kRestart:
		   this.game.restart();
		   break;
	   case kNewGame:
		   if(boardNum == kNumBoards)
		   {
			   boardNum = 1;
		   }
		   else
		   {
			   boardNum++;
		   }
		   
		   this.game.newGame(boardPrefSize, boardDifficulty, boardNum);
		   break;
	   case kSelectGame:
		   selectGame(scan);
		   break;
	   case kHighScores:
           writer.write("-- High Scores --\n");
           writer.write(this.hallOfFame.getHighScores());
           writer.flush();
		   break;
	   case kPeek:
		   this.game.peek();
		   break;
	   case kCheat:
		   this.game.cheat();
		   break;
	   case kAbout:
		   writer.write("-- About --");
		   writer.write("Kaboom Game by Erik Owen");
		   writer.flush();
		   break;
	   case kQuit:
		   scan.close();
		   break;
	   case kPreferences:
		   getNewPreferences(scan);
		   break;
	   default:
		   break;
	   }
   }
   
   private void selectGame(Scanner scan) throws IOException
   {
       String boardNumberString = "-1";
       int boardNumber;

       writer.write("Select Game: Enter desired game number (1 - 5000):\n");
       writer.flush();
       
       /*Only reads in from scanner if there is another line*/
       if(scan.hasNext())
       {
           boardNumberString = scan.nextLine();
       }
       
       boardNumber = Integer.parseInt(boardNumberString);
       
       /*Makes sure the board number is in the range of 1-5000*/
       if(boardNumber > 0 && boardNumber <= kNumBoards)
       {
    	   this.boardNum = boardNumber;
    	   this.game.newGame(boardPrefSize, boardDifficulty, boardNum);
       }
   }
   
   private void getNewPreferences(Scanner scan) throws IOException
   {
	   String userInput = "";
	   
	   writer.write("[Board Size]");
	   writer.write("(a) small = 8  (b) medium = 10  (c) large = 12  ");
	   writer.write("[Difficulty]");
	   writer.write("(d) easy = 8  (e) moderate = 6  (f) hard = 4  ");
	   
	   if(scan.hasNext())
	   {
		   userInput = scan.nextLine().trim().toLowerCase();
		   char firstChoice, secondChoice;
		   
		   if(userInput.length() <= 2)
		   {
			   for(char curPos : userInput.toCharArray())
			   {
				   switch(curPos)
				   {
				   case 'a':
					   this.boardPrefSize = kSmallBoard;
					   break;
				   case 'b':
					   this.boardPrefSize = kMediumBoard;
					   break;
				   case 'c':
					   this.boardPrefSize = kLargeBoard;
					   break;
				   case 'd':
					   this.boardDifficulty = kEasyDifficulty;
					   break;
				   case 'e':
					   this.boardDifficulty = kModerateDifficulty;
					   break;
				   case 'f':
					   this.boardDifficulty = kHardDifficulty;
					   break;
				   }
			   }
		   }
	   }
   }
   
   private void makeMove(String move)
   {
	   
   }
}
