import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.Timer;

/**
 * Class which contains the underlying logic of a Kaboom Game.
 * 
 * @author erikowen
 * @version 1
 *
 */
public class KaboomGame extends Observable
{

    private int boardSize, difficulty, boardNum, numMoves, flagCount,
    timeSeconds, winCount;
    private KaboomBoard board;
    private Timer timer;
    private boolean hasLost, hasWon;
    private static final int kMillisecondsPerSecond = 1000;
    public static final int kUpdateBoardLoss = 1, kUpdateBoardWin = 2, 
    kUpdateTimer = 3;
    
    /**
     * Constructor to instantiate a KaboomGame object.
     * 
     * @param boardSize the desired size of the Kaboom board.
     * @param difficulty the desired difficulty of the Kaboom game.
     * @param boardNum the desired board number for the Kaboom board.
     */
    public KaboomGame(int boardSize, int difficulty, int boardNum)
    {
        this.boardSize = boardSize;
        this.difficulty = difficulty;
        this.boardNum = boardNum;
        this.numMoves = 0;
        this.flagCount = 0;
        this.timeSeconds = 0;
        this.winCount = 0;
        this.hasLost = false;
        this.hasWon = false;
        
        ActionListener timerListener = new ActionListener()
        {   
            @Override
            public void actionPerformed(ActionEvent e)
            {
                timeSeconds++;
                setChanged();
                notifyObservers(kUpdateTimer);
            }
        };
        
        this.timer = new Timer(kMillisecondsPerSecond, timerListener);
        this.timer.setInitialDelay(kMillisecondsPerSecond);
    }
    
    /**
     * Creates a new Kaboom Game
     * 
     * @param desiredBoardSize the desired size of the Kaboom board.
     * @param desiredDifficulty the desired difficulty of the Kaboom game.
     * @param desiredBoardNum the desired board number for the Kaboom board.
     */
    public void newGame(int desiredBoardSize, int desiredDifficulty, int desiredBoardNum)
    {
        this.boardSize = desiredBoardSize;
        this.difficulty = desiredDifficulty;
        this.boardNum = desiredBoardNum;
        
        this.numMoves = 0;
        this.flagCount = 0;
        this.timeSeconds = 0;
        this.winCount = 0;
        this.hasLost = false;
        this.hasWon = false;
        this.board = new KaboomBoard(this.boardSize, this.difficulty,
            this.boardNum);
        this.timer.restart();
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Resets the board, time, moves, and number of flags for a game.
     */
    public void restart()
    {
        newGame(this.boardSize, this.difficulty, this.boardNum);
    }
    
    /**
     * Takes a turn on the Kaboom Board.
     * 
     * @param row the desired row in which the user selected
     * @param col the desired column in which the user selected
     */
    public void takeTurn(int row, int col)
    {
        /*Determines if the move is in the bounds of the board*/
        if(row >= 0 && row < board.getRowCount() && col >= 0 && col < 
            board.getColumnCount())
        {
            this.numMoves++;
            KaboomCell chosenCell = (KaboomCell)board.getValueAt(row, col);
            chosenCell.setUncovered();
            
            /*Determines if the cell chosen is a bomb*/
            if(chosenCell.isBomb())
            {
                this.timer.stop();
                this.hasLost = true;
                chosenCell.setCellState(KaboomPieces.bombHit);
                this.board.setToPeak();
                
                this.setChanged();
                this.notifyObservers(kUpdateBoardLoss);
            }
            else
            {   
                /*If cell is empty than all adjacent cells are removed too*/
                if(chosenCell.getNumBombsNear() == 0)
                {
                    this.board.uncoverNeighboringEmptyCells(row, col);
                }
                
                /*Determines if the user has won the baord*/
                if(board.boardIsCleared() && !this.hasWon && !this.hasLost)
                {
                    this.hasWon = true;
                    this.setChanged();
                    this.notifyObservers(kUpdateBoardWin);
                }
                else
                {
                    this.setChanged();
                    this.notifyObservers();
                }
            }
        }
    }
    
    /**
     * Accessor method to retrieve the Kaboom Board
     * @return the Kaboom Board
     */
    public KaboomBoard getBoard()
    {
        return this.board;
    }
    
    /**
     * Accessor method to get the number of moves
     * @return the number of moves the user has made
     */
    public int getMoveCount()
    {
        return this.numMoves;
    }
    
    /**
     * Accessor method to get the number of flags used
     * 
     * @return the number of flags used by the user
     */
    public int getFlagCount()
    {
        return this.flagCount;
    }
    
    /**
     * Accessor method to get the number of bombs on the board
     * 
     * @return the number of bombs on the board
     */
    public int getNumBombs()
    {
        return this.board.getNumBombs();
    }
    
    /**
     * Accessor method to get the current time the user has taken in the game
     * 
     * @return the number of seconds into the game
     */
    public int getTimerCount()
    {
        return this.timeSeconds;
    }
    
    /**
     * Toggles the flag at the current cell postion
     * @param row the row the cell is located
     * @param col the column the cell is located
     */
    public void toggleFlag(int row, int col)
    {
        KaboomCell flaggedCell = ((KaboomCell)this.board.getValueAt(row, col));
        
        /*Determines if the cell has already been flagged*/
        if(!flaggedCell.isFlagged())
        {
            /*Determines if the cell is flag-eligible*/
            if(flaggedCell.getCellState()  == KaboomPieces.covered)
            {
                this.flagCount++;
                flaggedCell.setFlagged();
            }
        }
        else
        {
            this.flagCount--;
            flaggedCell.setUnflagged();
        }
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Sets the board to the 'peek' state
     */
    public void peek()
    {
        this.board.setToPeak();
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Sets the board to the 'cheat' state
     */
    public void cheat()
    {
        this.board.createCheatBoard();
        
        this.setChanged();
        this.notifyObservers();
    }
    
}
