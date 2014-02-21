/**
 * Class representing a Kaboom Cell and it's contents
 * 
 * @author erikowen
 * @version 1
 *
 */
public class KaboomCell
{

    private KaboomPieces cellState;
    private boolean covered, flagged;
    private int bombsNear, row, col;
    
    /**
     * Constructor to instantiate a KaboomCell object
     * 
     * @param state the cell's state
     * @param row the row position of the cell on the Kaboom board
     * @param col the column position of the cell on the Kaboom board
     */
    public KaboomCell(KaboomPieces state, int row, int col)
    {
        this.cellState = state;
        this.bombsNear = 0;
        this.covered = true;
        this.flagged = false;
        this.row = row;
        this.col = col;
    }
    
    /**
     * Accessor method to get the cell's state
     * 
     * @return the cell's state or covered if it is still covered
     */
    public KaboomPieces getCellState()
    {
        KaboomPieces state;
        /*Determines if the current cell is covered or not*/
        if(this.covered)
        {
            state = KaboomPieces.covered;
        }
        else
        {
            state = this.cellState;
        }
        return state;
    }
    
    /**
     * Setter method to set the cell's state
     * 
     * @param state the state to set this cell to
     */
    public void setCellState(KaboomPieces state)
    {
        this.cellState = state;
    }
    
    /**
     * Determines if this cell is a bomb or not
     * @return true if bomb false otherwise
     */
    public boolean isBomb()
    {
        return (this.cellState == KaboomPieces.bomb ||
            this.cellState == KaboomPieces.bombHit);
    }
    
    /**
     * Sets the current cell to be uncovered
     */
    public void setUncovered()
    {
        this.covered = false;
        this.flagged = false;
    }
    
//    /**
//     * Sets the current cell to be covered
//     */
//    public void setCovered()
//    {
//        this.covered = true;
//    }
    
    /**
     * Sets the current cell to be flagged
     */
    public void setFlagged()
    {
        this.flagged = true;
    }
    
    /**
     * Sets the current cell to be unflagged
     */
    public void setUnflagged()
    {
        this.flagged = false;
    }
    
    /**
     * Accessor method to determine if this cell is flagged or not
     * @return true if cell is flagged, false otherwise.
     */
    public boolean isFlagged()
    {
        return this.flagged;
    }
    
    /**
     * Sets the number of bombs surrounding this cell
     * @param numBombsNear the number of bombs surrounding the cell
     */
    public void setNumBombsNear(int numBombsNear)
    {
        this.bombsNear = numBombsNear;
    }
    
    /**
     * Accessor method to get the number of bombs surrounding this cell.
     * @return int of the number of bombs surrounding this cell
     */
    public int getNumBombsNear()
    {
        return this.bombsNear;
    }
    
    /**
     * Gets the row of this cell
     * @return int of the row of this cell
     */
    public int getRow()
    {
        return this.row;
    }
    
    /**
     * Gets the column of this cell
     * @return int of the column of this cell
     */
    public int getColumn()
    {
        return this.col;
    }
}
