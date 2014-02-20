
public class KaboomCell {

	private KaboomPieces cellState;
	private boolean covered, flagged;
	private int bombsNear, row, col;
	
	public KaboomCell(KaboomPieces state, int row, int col)
	{
		this.cellState = state;
		this.bombsNear = 0;
		this.covered = true;
		this.flagged = false;
		this.row = row;
		this.col = col;
	}
	
	public KaboomPieces getCellState()
	{
		return this.covered ? KaboomPieces.covered : this.cellState;
	}
	
	public void setCellState(KaboomPieces state)
	{
		this.cellState = state;
	}
	
	public boolean isBomb()
	{
		return (this.cellState == KaboomPieces.bomb || this.cellState == KaboomPieces.bombHit);
	}
	
	public void setUncovered()
	{
		this.covered = false;
		this.flagged = false;
	}
	
	public void setCovered()
	{
		this.covered = true;
	}
	
	public void setFlagged()
	{
		this.flagged = true;
	}
	
	public void setUnflagged()
	{
		this.flagged = false;
	}
	
	public boolean isFlagged()
	{
		return this.flagged;
	}
	
	public void setNumBombsNear(int numBombsNear)
	{
		this.bombsNear = numBombsNear;
	}
	
	public int getNumBombsNear()
	{
		return this.bombsNear;
	}
	
	public int getRow()
	{
		return this.row;
	}
	
	public int getColumn()
	{
		return this.col;
	}
}
