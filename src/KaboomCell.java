
public class KaboomCell {

	private KaboomPieces cellState;
	private boolean covered;
	private int bombsNear, row, col;
	
	public KaboomCell(KaboomPieces state, int row, int col)
	{
		this.cellState = state;
		this.bombsNear = 0;
		this.covered = true;
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
		return this.cellState == KaboomPieces.bomb;
	}
	
	public void setUncovered()
	{
		this.covered = false;
	}
	
	public void setCovered()
	{
		this.covered = true;
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
