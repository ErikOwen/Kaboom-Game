import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ini4j.Ini;


public class KaboomConsole implements Observer{
	private int boardNum, boardPrefSize, boardDifficulty;
	private Preferences prefs;
	private HighScores hallOfFame;
	private Reader reader;
	private Writer writer;
	private Scanner scan;
	private KaboomGame game;
	private static final int kNumBoards = 5000, kRestart = 1, kNewGame = 2,
		kSelectGame = 3, kHighScores = 4, kPeek = 5, kCheat = 6, kAbout = 7,
		kQuit = 8, kPreferences = 9, kNumOptions = 9, kSmallBoard = 8,
		kMediumBoard = 10, kLargeBoard = 12, kEasyDifficulty = 8, kModerateDifficulty = 6,
		kHardDifficulty = 4, kCharToInt = 65, secondsInAMin = 60, deciSystem = 10,
		kMaxNameLength = 20;
	
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
           this.boardPrefSize = prefs.getDefaultBoardSize();
           this.boardDifficulty = prefs.getDefaultDifficulty();
           
           this.game = new KaboomGame(this.boardPrefSize, this.boardDifficulty, this.boardNum);
           
           this.game.addObserver(this);
           
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
       this.scan = new Scanner(this.reader);
   }
   
   /**
    * Begins a Kaboom Game.
    */
   public void run()
   {
	   this.game.newGame(boardPrefSize, boardDifficulty, boardNum);
	   String userInput = "";
	   int userChoice = 0;
	   //Scanner scan = new Scanner(this.reader);
	   
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
					   executeOption(userChoice);
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
   
   private void executeOption(int optionNumber) throws IOException
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
		   writer.write("-- About --\n");
		   writer.write("Kaboom Game by Erik Owen\n");
		   writer.flush();
		   break;
	   case kQuit:
		   scan.close();
		   break;
	   case kPreferences:
		   getNewPreferences();
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
   
   private void getNewPreferences() throws IOException
   {
	   String userInput = "";
	   Ini.Section boardSizeSection = this.prefs.getBoardSizes();
	   Ini.Section difficultiesSection = this.prefs.getDifficulties();
	   char startChoice = 'a';
	   SortedSet<String> boardSizeKeys = new TreeSet<String>(boardSizeSection.keySet());
	   SortedSet<String> difficultyKeys = new TreeSet<String>(difficultiesSection.keySet());
	   Map<Character, String> choiceMap = new HashMap<Character, String>();
	   String boardSizeString = "", difficultyString = "";
	   
	   for(String key : boardSizeKeys)
	   {
		   choiceMap.put(startChoice, key);
		   boardSizeString += "(" + startChoice++ + ") " + key + " = " + boardSizeSection.get(key) + "  ";
	   }
	   
	   for(String key : difficultyKeys)
	   {
		   choiceMap.put(startChoice, key);
		   difficultyString += "(" + startChoice++ + ") " + key + " = " + difficultiesSection.get(key) + "  ";
	   }
	
	   writer.write("[Board Size]\n");
	   writer.write(boardSizeString + "\n");
	   writer.write("[Difficulty]\n");
	   writer.write(difficultyString + "\n");
	   writer.flush();
	   
	   if(scan.hasNext())
	   {
		   userInput = scan.nextLine().trim().toLowerCase();
		   
		   if(userInput.length() <= 2)
		   {
			   for(char curPos : userInput.toCharArray())
			   {
				   if(choiceMap.containsKey(curPos) && boardSizeSection.containsKey(choiceMap.get(curPos)))
				   {
					   this.boardPrefSize = Integer.parseInt(boardSizeSection.get(choiceMap.get(curPos)));
				   }
				   else if(choiceMap.containsKey(curPos) && difficultiesSection.containsKey(choiceMap.get(curPos)))
				   {
					   this.boardDifficulty = Integer.parseInt(difficultiesSection.get(choiceMap.get(curPos)));
				   }
			   }
		   }
	   }
	   
	   this.game.newGame(boardPrefSize, boardDifficulty, boardNum);
   }
   
   private void makeMove(String move)
   {
	   boolean addFlag = false;
	   int row, col;
	   move = move.toLowerCase();
	   
	   if(move.charAt(0) == '.')
	   {
		   addFlag = true;
		   move = move.substring(1);
	   }
	   
	   row = move.substring(0, 1).toUpperCase().charAt(0) - kCharToInt;
       col= Integer.parseInt(move.substring(1, move.length())) - 1;
       
       if(row >= 0 && row < game.getBoard().getRowCount() && col >= 0 && col < game.getBoard().getColumnCount())
       {
           if(addFlag)
           {
        	   game.toggleFlag(row, col);
           }
           else
           {
        	   game.takeTurn(row, col);
           }
       }
   }
   
   private Character mapBoardPieceToCharacter(KaboomCell cell)
   {
	   Character retChar = ' ';
	   
	   if(cell.getCellState() == KaboomPieces.flagged)
	   {
		   retChar = '@';
	   }
	   else if(cell.getCellState() == KaboomPieces.covered)
	   {
		   retChar = '-';
	   }
	   else
	   {
		   if(cell.getCellState() == KaboomPieces.bombHit)
		   {
			   retChar = '*';
		   }
		   else if(cell.getCellState() == KaboomPieces.bomb)
		   {
			   retChar = 'B';
		   }
		   else if(cell.getNumBombsNear() > 0)
		   {
			   String charStr = (new Integer(cell.getNumBombsNear())).toString();
			   retChar = charStr.charAt(0);
		   }
	   }
	   
	   return retChar;
   }
   
   private void displayBoardAndOptions() throws IOException
   {
	   String timeString = "" + game.getTimerCount() / secondsInAMin + ":" + String.format("%02d", game.getTimerCount() % secondsInAMin);
       writer.write("Kaboom - board " + this.boardNum + "\n");
       writer.write("Moves: " + game.getMoveCount() + "   Flags: " + game.getFlagCount() + "/" + game.getNumBombs() + "  " + timeString + "\n");
       String colString = "     ";
       
       /*Creates the top row of numbers on the board*/
       for(int colIter = 1; colIter < game.getBoard().getColumnCount(); colIter++)
       {
           colString = colString.concat((colIter % deciSystem) + "  ");
       }
       colString = colString.concat((game.getBoard().getColumnCount() % deciSystem) + "\n");
       writer.write(colString);
       
       char curLetter = 'A';
       String curRow = "";
       
       /*Iterates through each row ont he board*/
       for(int rowIter = 0; rowIter < game.getBoard().getRowCount(); rowIter++, curLetter++)
       {
           curRow = curRow.concat(" " + curLetter + ":  ");
           
           /*Creates each row of the board by concatenating all of the columns*/
           for(int colIter = 0; colIter < game.getBoard().getColumnCount(); colIter++)
           {
        	   KaboomCell curPiece = (KaboomCell)game.getBoard().getValueAt(rowIter, colIter);
        	   Character curSpot = mapBoardPieceToCharacter(curPiece);
        	   
               curRow = curRow.concat(curSpot.toString());
                   
               /*Makes sure spacing is correct*/
               if(colIter < game.getBoard().getColumnCount() - 1)
               {
                   curRow = curRow.concat("  ");
               }
           }
               
           curRow = curRow.concat("\n");
           writer.write(curRow);
           curRow = "";
       }
           
       String dashedLine = " ----";
       
       /*Creates the dashed line's size depending on the size of the board*/
       for(int dashNdx = 0; dashNdx < game.getBoard().getColumnCount() - 1; dashNdx++)
       {
           dashedLine = dashedLine.concat("---");
       }
           
       dashedLine = dashedLine.concat("-\n");
       writer.write(dashedLine);
       writer.write("1)Restart 2)New 3)Select 4)Scores 5)Peek 6)Cheat 7)About 8)Quit 9)Prefs\n");
       writer.flush();
   }
   
   @Override
   public void update(Observable o, Object arg) {
	   String saveScoreResponse, userName;
	   try
	   {
		   displayBoardAndOptions();
		   
		   if(game.isGameOver())
		   {
			   if(game.gameWon())
			   {
				   writer.write("Game Won Notification: Game " + this.boardNum + " Cleared! \n");
				   String timerString = "" + game.getTimerCount() / secondsInAMin + ":" + String.format("%02d", game.getTimerCount() % secondsInAMin);
				   writer.write("Save your time of " + timerString + "? (y/n)");
				   writer.flush();
				   
				   if(scan.hasNext())
				   {
					   saveScoreResponse = scan.nextLine().trim().toLowerCase();
					   if(saveScoreResponse.equals("y"))
					   {
						   writer.write("Name Entry: Your score of " + timerString + " will be entered into the Hall of Fame. \n");
						   writer.write("Enter your name: ");
						   writer.flush();
						   
						   userName = scan.nextLine();
						   
				           if(userName.length() > kMaxNameLength)
				           {
				               userName = userName.substring(0, kMaxNameLength);
				           }
				            
						   this.hallOfFame.addHighScore(userName, game.getTimerCount());
					   }
				   }
			   }
			   else
			   {
				   writer.write("-- Game Over --\n");
				   writer.write("You lost.\n");
				   writer.flush();
			   }
		   }
	   }
	   catch (IOException e)
	   {
		   e.printStackTrace();
	   }
   }
}
