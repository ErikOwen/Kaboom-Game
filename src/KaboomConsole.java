import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.ini4j.Ini;

/**
 * Class which contains all the underlying functionality to run
 * the Kaboom game through the console
 * 
 * @author Erik Owen
 * @version 1
 */
public class KaboomConsole implements Observer
{
    private int boardNum, boardPrefSize, boardDifficulty;
    private Preferences prefs;
    private HighScores hallOfFame;
    private Reader reader;
    private Writer writer;
    private Scanner scan;
    private KaboomGame game;
    private static final int kNumBoards = 5000, kRestart = 1, kNewGame = 2,
    kSelectGame = 3, kHighScores = 4, kPeek = 5, kCheat = 6, kAbout = 7,
    kQuit = 8, kPreferences = 9, kNumOptions = 9, kCharToInt = 65,
    kSecondsInAMin = 60, kDeciSystem = 10, kMaxNameLength = 20,
    kHiddenEraseHighScores = 0;
    
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
           
            this.game = new KaboomGame(this.boardPrefSize, this.boardDifficulty,
                this.boardNum);
           
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
        int userChoice = -1;
       
        /*Reads input until quit is chosen or there is no more input left*/
        while(userChoice != kQuit && scan.hasNext())
        {
            userInput = scan.nextLine().trim();
            try
            {
                /*If length is 1 then user is choosing an option*/
                if(userInput.length() == 1)
                {
                    userChoice = Integer.parseInt(userInput);
                   
                    /*Executes option if it is in valid option range*/
                    if(userChoice >= 0 && userChoice <= kNumOptions)
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
                userInput = "";
            }
        }
    }
   
   /**
    * Executes the desired option passed in.
    * @param optionNumber the desired option
    * @throws IOException thrown by the writer
    */
    private void executeOption(int optionNumber) throws IOException
    {
        /*Determines which option is chosen*/
        switch(optionNumber)
        {
            case kHiddenEraseHighScores:
                this.hallOfFame.eraseHighScores();
                this.writer.write("kaboom/halloffame.ser deleted.\n");
                this.writer.flush();
                break;
            case kRestart:
                this.game.restart();
                break;
            case kNewGame:
                newGame();
                break;
            case kSelectGame:
                selectGame();
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
   
    private void newGame()
    {
        /*Sets board number to 1 if at 5000*/
        if(boardNum == kNumBoards)
        {
            boardNum = 1;
        }
        else
        {
            boardNum++;
        }
           
        this.game.newGame(boardPrefSize, boardDifficulty, boardNum);
    }
    
   /**
    * Selects a board specified by the user
    * 
    * @param scan the scanner used to read input
    * @throws IOException thrown by the writer
    */
    private void selectGame() throws IOException
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
   
   /**
    * Gets new preferences specified by the user: either new board size of
    * difficulty.
    * 
    * @throws IOException thrown by the writer
    */
    private void getNewPreferences() throws IOException
    {
        String userInput = "", boardSizeString = "", difficultiesString = "";
        final Ini.Section boardSizeSection = this.prefs.getBoardSizes();
        final Ini.Section difficultiesSection = this.prefs.getDifficulties();
        char startChoice = 'a';
        Map<Character, String> choiceMap = new TreeMap<Character, String>();
        Set<String> sections = this.prefs.getIni().keySet();
        String prefsString = "";
        
        /*Iterates throug all of the sections in the preferences file*/
        for(String section : sections)
        {
            Ini.Section curSection = this.prefs.getIni().get(section);
            prefsString += ("[" + curSection.getName() + "]" + "\n");
            
            /*Iterates through each element in the section*/
            for(String sectionElementKey : curSection.keySet())
            {
                choiceMap.put(startChoice, sectionElementKey);
                prefsString += "(" + startChoice++ + ") " + sectionElementKey +
                        " = " + curSection.get(sectionElementKey) + "  ";
            }
            
            prefsString += "\n";
        }
      
        boolean firstBoard = true, firstDifficulty = true;
        /*Iterates through all of the board options*/
        for(String boardKey : boardSizeSection.keySet())
        {
            /*If it is the first option then set it to the default board size*/
            if(firstBoard)
            {
                this.boardPrefSize = Integer.parseInt(boardSizeSection.get(boardKey));
                firstBoard = false;
            }
        }
        /*Iterates through all of the difficultiy options*/
        for(String difficultyKey : difficultiesSection.keySet())
        {
            /*If it is the first option then set it to the default difficulty*/
            if(firstDifficulty)
            {
                this.boardDifficulty = Integer.parseInt(
                    difficultiesSection.get(difficultyKey));
                firstDifficulty = false;
            }
        }
        prefsString += "Your choice?\n";
        writer.write(prefsString);
        writer.flush();
        
        getPreferenceInput(userInput, choiceMap, boardSizeSection, difficultiesSection);
        this.game.newGame(boardPrefSize, boardDifficulty, boardNum);
    }
    
    /**
     * Helper method for the getNewPreferences method
     */
    private void getPreferenceInput(String userInput, Map<Character, String> choiceMap, 
        Ini.Section boardSizeSection, Ini.Section difficultiesSection)
    {
        /*Determines if there is any more input left to read in*/
        if(scan.hasNext())
        {
            userInput = scan.nextLine().trim().toLowerCase();
           
            /*Makes sure the user input is valid length*/
            if(userInput.length() <= 2)
            {
                /*Iterates through  the characters of the user input*/
                for(char curPos : userInput.toCharArray())
                {
                    /*Determines if the user's choice is a valid option*/
                    if(choiceMap.containsKey(curPos) &&
                        boardSizeSection.containsKey(choiceMap.get(curPos)))
                    {
                        this.boardPrefSize = Integer.parseInt(
                            boardSizeSection.get(choiceMap.get(curPos)));
                    }
                    /*Determines if the user's choice is a valid option*/
                    else if(choiceMap.containsKey(curPos) && 
                        difficultiesSection.containsKey(choiceMap.get(curPos)))
                    {
                        this.boardDifficulty = Integer.parseInt(
                            difficultiesSection.get(choiceMap.get(curPos)));
                    }
                }
            }
        }
    }
   
   /**
    * Makes a move in the kaboom game.
    * 
    * @param move the move specified by the user's string
    */
    private void makeMove(String move)
    {
        boolean addFlag = false;
        int row, col;
        move = move.toLowerCase();
       
        /*Determines if user's move is toggling a flag*/
        if(move.charAt(0) == '.')
        {
            addFlag = true;
            move = move.substring(1);
        }
       
        row = move.substring(0, 1).toUpperCase().charAt(0) - kCharToInt;
        col = Integer.parseInt(move.substring(1, move.length())) - 1;
       
        /*Determines if the users move is in the board's range*/
        if(row >= 0 && row < game.getBoard().getRowCount() && col >= 0 &&
            col < game.getBoard().getColumnCount())
        {
            /*Adds a flag if desired, otherwise a move is made*/
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
   
   /**
    * Helper method which maps a KaboomPieces piece to a character for the
    * console version.
    * 
    * @param cell the specified cell to be converted to a character
    * @return a character representing the state of the KaboomCell.
    */
    private Character mapBoardPieceToCharacter(KaboomCell cell)
    {
        Character retChar = ' ';
       
        /*Determines if the cell is flagged or not*/
        if(cell.isFlagged())
        {
            retChar = '@';
        }
        /*Determines if the cell is covered or not*/
        else if(cell.getCellState() == KaboomPieces.covered)
        {
            retChar = '-';
        }
        else
        {
            /*Determines if the cell is a bomb that has been hit*/
            if(cell.getCellState() == KaboomPieces.bombHit)
            {
                retChar = '*';
            }
            /*Determines if the cell is a bomb that has not been hit*/
            else if(cell.getCellState() == KaboomPieces.bomb)
            {
                retChar = 'B';
            }
            /*Uncovers the cell number and determines number of bombs*/
            else if(cell.getNumBombsNear() > 0)
            {
                String charStr = (new Integer(
                    cell.getNumBombsNear())).toString();
                retChar = charStr.charAt(0);
            }
        }
       
        return retChar;
    }
   
   /**
    * Helper method to display the board and options
    * 
    * @throws IOException thrown by the writer
    */
    private void displayBoardAndOptions() throws IOException
    {
        String timeString = "" + game.getTimerCount() / kSecondsInAMin + ":" 
            + String.format("%02d", game.getTimerCount() % kSecondsInAMin);
        writer.write("Kaboom - board " + this.boardNum + "\n");
        writer.write("Moves: " + game.getMoveCount() + "   Flags: " + 
            game.getFlagCount() + "/" + game.getNumBombs() + 
            "  " + timeString + "\n");
        String colString = "     ";
       
        /*Creates the top row of numbers on the board*/
        for(int colIter = 1; colIter < game.getBoard().getColumnCount(); 
            colIter++)
        {
            colString = colString.concat((colIter % kDeciSystem) + "  ");
        }
        colString = colString.concat((game.getBoard().getColumnCount() % 
            kDeciSystem) + "\n");
        writer.write(colString);
       
        char curLetter = 'A';
        String curRow = "";
       
        /*Iterates through each row ont he board*/
        for(int rowIter = 0; rowIter < game.getBoard().getRowCount(); 
            rowIter++, curLetter++)
        {
            curRow = curRow.concat(" " + curLetter + ":  ");
           
            /*Creates each row of the board by concatenating all of the columns*/
            for(int colIter = 0; colIter < game.getBoard().getColumnCount();
                colIter++)
            {
                KaboomCell curPiece = (KaboomCell)game.getBoard().getValueAt(
                    rowIter, colIter);
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
        for(int dashNdx = 0; dashNdx < game.getBoard().getColumnCount() - 1; 
            dashNdx++)
        {
            dashedLine = dashedLine.concat("---");
        }
           
        dashedLine = dashedLine.concat("-\n");
        writer.write(dashedLine);
        writer.write("1)Restart 2)New 3)Select 4)Scores 5)Peek 6)Cheat " +
            "7)About 8)Quit 9)Prefs\n");
        writer.flush();
    }
   
   /**
    * Updates the console's output based on the status of the game
    * 
    * @param o the object being observed
    * @param arg an argument being passed by the observable object
    */
    @Override
    public void update(Observable o, Object arg)
    {
        String saveScoreResponse, userName;
        Integer response = (Integer) arg;

        try
        {
            displayBoardAndOptions();
           
            /*Determines if the player has lost*/
            if(response != null && response == KaboomGame.kUpdateBoardLoss)
            {
                writer.write("-- Game Over --\n");
                writer.write("You lost.\n");
                writer.flush();
            }
            /*Determines if the player has won*/
            else if(response != null && response == KaboomGame.kUpdateBoardWin)
            {
                writer.write("Game Won Notification: Game " + this.boardNum + 
                    " Cleared! \n");
                String timerString = "" + game.getTimerCount() / kSecondsInAMin + 
                    ":" + String.format("%02d", game.getTimerCount() 
                        % kSecondsInAMin);
                writer.write("Save your time of " + timerString + "? (y/n)\n");
                writer.flush();
               
                /*Determines if there is more input to read*/
                if(scan.hasNext())
                {
                    saveScoreResponse = scan.nextLine().trim().toLowerCase();
                   
                    /*Determines if the user wants to save their score*/
                    if(saveScoreResponse.equals("y"))
                    {
                        writer.write("Name Entry: Your score of " + timerString 
                            + " will be entered into the Hall of Fame. \n");
                        writer.write("Enter your name: \n");
                        writer.flush();
                       
                        userName = scan.nextLine();
                       
                        /*Truncates the name if it is too long*/
                        if(userName.length() > kMaxNameLength)
                        {
                            userName = userName.substring(0, kMaxNameLength);
                        }
                        
                        this.hallOfFame.addHighScore(userName,
                            game.getTimerCount());
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
