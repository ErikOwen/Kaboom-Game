import java.util.LinkedList;
import java.util.Random;

import javax.swing.table.AbstractTableModel;

/**
 * Class containing the logic for the Kaboom board
 * 
 * @author erikowen
 * @version 1
 *
 */
public class KaboomBoard extends AbstractTableModel
{
    private KaboomCell[][] board;
    private int boardSize, difficulty, boardNum, numBombs;
    private String[] columnNames;
    
    /**
     * Instantiates a new KaboomBoard
     * 
     * @param boardSize the desired board size
     * @param difficulty the desired difficulty
     * @param boardNum the desired boardNum
     */
    public KaboomBoard(int boardSize, int difficulty, int boardNum)
    {
        this.boardSize = boardSize;
        this.difficulty = difficulty;
        this.boardNum = boardNum;
        
        this.generateBoard();
        
        this.columnNames = new String[boardSize];
        
        /*Sets all of the column names to empty value*/
        for(String str : this.columnNames)
        {
            str = "";
        }
    }
    
    /**
     * Initializes a Kaboom Board
     */
    private void generateBoard()
    {
        int numGeneratedBombs = (this.boardSize * this.boardSize) / 
            this.difficulty;
        Random generator = new Random(boardNum);
        
        this.board = new KaboomCell[boardSize][boardSize];
        
        /*Iterates through all of the bombs and adds them to the board*/
        for(int iter = 0; iter < numGeneratedBombs; iter++)
        {
            int curRow = generator.nextInt(boardSize);
            int curCol = generator.nextInt(boardSize);
            
            /*Checks if the current spot doesn't have a bomb on it yet*/
            if(board[curRow][curCol] == null)
            {
                board[curRow][curCol] = new KaboomCell(KaboomPieces.bomb, 
                    curRow, curCol);
                this.numBombs++;
            }
        }
        
        /*Iterates through all of the rows*/
        for(int rowIter = 0; rowIter < board.length; rowIter++)
        {
            /*Iterates through all of the columns*/
            for(int colIter = 0; colIter < board[0].length; colIter++)
            {
                /*sets the cell state if spot is empty*/
                if(board[rowIter][colIter] == null)
                {
                    setCellState(rowIter, colIter);
                }
            }
        }
        
    }
    
    /**
     * Accessor method to get the number of bombs on the board
     * @return int specifying the number of bombs on the board
     */
    public int getNumBombs()
    {
        return this.numBombs;
    }
    
    /**
     * Helper method that sets all of the cell's states that aren't bombs
     * @param row the current row of the cell
     * @param col the current column of the cell
     */
    private void setCellState(int row, int col)
    {
        board[row][col] = new KaboomCell(KaboomPieces.empty, row, col);
        int numBombsNear = 0;
        int[] rowDirections = {-1, 0, 1};
        int[] colDirections = {-1, 0, 1};

        /*Iterates through all of the different neighboring rows*/
        for(int rDir : rowDirections)
        {
            /*Iterates through all of the neighboring columns*/
            for(int cDir : colDirections)
            {
                numBombsNear += checkDirectionForBombs(row, col, rDir, cDir);
            }
        }
        
        board[row][col].setNumBombsNear(numBombsNear);
    }
    
    /**
     * Helper method that checks a specified direction for bombs
     * 
     * @param row the row of the cell being examined
     * @param col the column of the cell being examined
     * @param rDir the row direction to search for bombs
     * @param cDir the column direction to search for bombs
     * @return 1 if there is a bomb, 0 otherwise.
     */
    private int checkDirectionForBombs(int row, int col, int rDir, int cDir)
    {
        int retVal = 0;
        /*Makes sure current cell is not being checked for being a bomb*/
        if(rDir != 0 || cDir != 0)
        {
            /*Makes sure row direction stays in bounds*/
            if(row + rDir >= 0 && row + rDir < board.length)
            {
                /*Makes sure column direction stays in bounds*/
                if(col + cDir >= 0 && col + cDir < board[0].length)
                {
                    /*Sees if spot being checked is a bomb or not*/
                    if(board[row + rDir][col + cDir] != null && 
                        board[row + rDir][col + cDir].isBomb())
                    {
                        retVal = 1;
                    }
                }
            }
        }
        
        return retVal;
    }
    
    /**
     * Helper method to see if there are empty neighbors
     * @param row the current cell's row position
     * @param col the current cell's column position
     * @param rDir the row's direction which is being checked for emptyness
     * @param cDir the column's direction which is being checked for emptyness
     * @param queue the queue to add an empty cell to
     */
    private void checkForEmptyNeighbors(int row, int col, int rDir,
        int cDir, LinkedList<KaboomCell> queue)
    {
        /*Makes sure to not add self to queue*/
        if(rDir != 0 || cDir != 0)
        {
            /*makes sure row direction stays in bounds*/
            if(row + rDir >= 0 && row + rDir < board.length)
            {
                /*makes sure column direction stays in bounds*/
                if(col + cDir >= 0 && col + cDir < board[0].length)
                {
                    /*Determines if current spot is covered and not a bomb*/
                    if(board[row + rDir][col + cDir] != null && board[row + 
                        rDir][col + cDir].getCellState() == 
                        KaboomPieces.covered && !board[row + rDir][col + 
                        cDir].isBomb())
                    {
                        /*Checks to see if current spot is also empty*/
                        if(board[row + rDir][col + cDir].getNumBombsNear() == 0)
                        {
                            queue.add((KaboomCell)board[row + rDir]
                                [col + cDir]);
                        }
                        else
                        {
                            board[row + rDir][col + cDir].setUncovered();
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Method which uncovers empty cells if a cell is empty
     * @param row the current cell row being checked for empty neighbors
     * @param col the current cell column being checked for empty neighbors
     */
    public void uncoverNeighboringEmptyCells(int row, int col)
    {
        int[] rowDirections = {-1, 0, 1};
        int[] colDirections = {-1, 0, 1};
        KaboomCell curCell;
        LinkedList<KaboomCell> queue = new LinkedList<KaboomCell>();
        queue.add((KaboomCell)this.getValueAt(row, col));
        
        /*Continues to add empty cells to queue until there are none left*/
        while(!queue.isEmpty())
        {
            curCell = queue.remove();
            curCell.setUncovered();

            /*Iterates through the row directions*/
            for(int rDir : rowDirections)
            {
                /*Iterates through the column directions*/
                for(int cDir : colDirections)
                {
                    checkForEmptyNeighbors(curCell.getRow(),
                        curCell.getColumn(), rDir, cDir, queue);
                }
            }
        }
    }
    
    /**
     * Sets the Kaboom board to the cheat formation
     */
    public void createCheatBoard()
    {
        /*Iterates through all of the rows*/
        for(int rowIter = 0; rowIter < board.length; rowIter++)
        {
            /*Iterates through all of the columns*/
            for(int colIter = 0; colIter < board[0].length; colIter++)
            {
                board[rowIter][colIter] = new KaboomCell(KaboomPieces.empty, 
                    rowIter, colIter);
                board[rowIter][colIter].setUncovered();
            }
        }
        
        board[0][0] = new KaboomCell(KaboomPieces.bomb, 0, 0);
        board[0][1] = new KaboomCell(KaboomPieces.empty, 0, 1);
        board[0][1].setNumBombsNear(1);
    }
    
    /**
     * Sets the Kaboom board to the peek formation
     */
    public void setToPeak()
    {
        /*Iterates through all of the rows*/
        for(int rowIter = 0; rowIter < board.length; rowIter++)
        {
            /*Iterates through all of the columns*/
            for(int colIter = 0; colIter < board[0].length; colIter++)
            {
                board[rowIter][colIter].setUncovered();
            }
        }
    }
    
    /**
     * Checks to see if the board is cleared
     * @return true if board is cleared, false if not cleared.
     */
    public boolean boardIsCleared()
    {
        boolean boardCleared = true;
        
        /*Iterates through all of the rows*/
        for(int rowIter = 0; rowIter < board.length && boardCleared; rowIter++)
        {
            /*Iterates through all of the columns*/
            for(int colIter = 0; colIter < board[0].length && boardCleared;
                colIter++)
            {
                /*Sees if there is a covered cell that is not a bomb*/
                if(board[rowIter][colIter].getCellState() == 
                    KaboomPieces.covered && !board[rowIter][colIter].isBomb())
                {
                    boardCleared = false;
                }
            }
        }
        
        return boardCleared;
    }
    
    /**
     * Getter method for the specified column name.
     * 
     * @param col the column number.
     * @return the specified column name.
     */
//    public String getColumnName(int col)
//    {
//        return columnNames[col];
//    }
    
    /**
     * Returns the number of rows on the board
     * 
     * @return int of the number of rows
     */
    @Override
    public int getRowCount()
    {
        return this.board.length;
    }

    /**
     * Returns the number of columns on the board
     * 
     * @return int of the number of columns
     */
    @Override
    public int getColumnCount()
    {
        return this.board[0].length;
    }

    /**
     * Returns the value at the current cell's position
     * 
     * @param rowIndex the row of the cell being returned
     * @param columnIndex the column of the cell being returned
     * @return a KaboomCell of the object at the specified position
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return this.board[rowIndex][columnIndex];
    }
    
    /**
     * Getter method for the class type in the specified column.
     * 
     * @param col the column number.
     * @return the Class in this column.
     */
    public Class getColumnClass(int col)
    {
        return getValueAt(0, col).getClass();
    }

}
