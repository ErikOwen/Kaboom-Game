
public class KaboomCell {

	private KaboomPieces cellState;
	private boolean covered;
	private int bombsNear;
	
	public KaboomCell(KaboomPieces state)
	{
		this.cellState = state;
		this.bombsNear = 0;
		this.covered = true;
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
	
	public void setNumBombsNear(int numBombsNear)
	{
		this.bombsNear = numBombsNear;
	}
	
	public int getNumBombsNear()
	{
		return this.bombsNear;
	}
}
