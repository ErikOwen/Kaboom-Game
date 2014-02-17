import java.util.LinkedList;
import java.util.Random;

import javax.swing.table.AbstractTableModel;


public class KaboomBoard extends AbstractTableModel {

	private KaboomCell[][] board;
	private int boardSize, difficulty, boardNum;
	private String[] columnNames;
	
	public KaboomBoard(int boardSize, int difficulty, int boardNum)
	{
		this.boardSize = boardSize;
		this.difficulty = difficulty;
		this.boardNum = boardNum;
		
		this.generateBoard();
		
        for(String str : this.columnNames)
        {
            str = "";
        }
	}
	
	private void generateBoard()
	{
		int numBombs = (this.boardSize * this.boardSize) / this.difficulty;
		Random generator = new Random(boardNum);
		
		this.board = new KaboomCell[boardSize][boardSize];
		
		for(int iter = 0; iter < numBombs; iter++)
		{
			board[generator.nextInt()][generator.nextInt()] = new KaboomCell(KaboomPieces.bomb);
		}
		
		for(int rowIter = 0; rowIter < board.length; rowIter++)
		{
			for(int colIter = 0; colIter < board[0].length; colIter++)
			{
				if(board[rowIter][colIter] == null)
				{
					setCellState(rowIter, colIter);
				}
			}
		}
		
	}
	
	private void setCellState(int row, int col)
	{
		board[row][col] = new KaboomCell(KaboomPieces.empty);
		int numBombsNear = 0;
		int[] rowDirections = {-1, 0, 1};
		int[] colDirections = {-1, 0, 1};

		for(int rDir : rowDirections)
		{
			for(int cDir : colDirections)
			{
				numBombsNear += checkDirectionForBombs(row, col, rDir, cDir);
			}
		}
		
		board[row][col].setNumBombsNear(numBombsNear);
	}
	
	private int checkDirectionForBombs(int row, int col, int rDir, int cDir)
	{
		int retVal = 0;
		if(col != 0 || row != 0)
		{
			if(row + rDir > 0 && row + rDir < board.length)
			{
				if(col + cDir > 0 && col + cDir < board[0].length)
				{
					if(board[row + rDir][col + cDir] != null && board[row + rDir][col + cDir].isBomb()) {
						retVal = 1;
					}
				}
			}
		}
		
		return retVal;
	}
	
	private void checkForEmptyNeighbors(int row, int col, int rDir, int cDir, LinkedList<KaboomCell> queue)
	{
		if(col != 0 || row != 0)
		{
			if(row + rDir > 0 && row + rDir < board.length)
			{
				if(col + cDir > 0 && col + cDir < board[0].length)
				{
					if(board[row + rDir][col + cDir] != null && board[row + rDir][col + cDir].getNumBombsNear() == 0) {
						queue.add((KaboomCell)board[row + rDir][col + cDir]);
					}
				}
			}
		}
	}
	
	public void uncoverNeighboringEmptyCells(int row, int col)
	{
		int[] rowDirections = {-1, 0, 1};
		int[] colDirections = {-1, 0, 1};
		KaboomCell curCell;
		LinkedList<KaboomCell> queue = new LinkedList<KaboomCell>();
		queue.add((KaboomCell)this.getValueAt(row, col));
		
		while(!queue.isEmpty())
		{
			curCell = queue.remove();
			curCell.setUncovered();
			
			for(int rDir : rowDirections)
			{
				for(int cDir : colDirections)
				{
					checkForEmptyNeighbors(row, col, rDir, cDir, queue);
				}
			}
			
			
		}
	}
	
    /**
     * Getter method for the specified column name.
     * 
     * @param col the column number.
     * @return the specified column name.
     */
    public String getColumnName(int col)
    {
        return columnNames[col];
    }
	
	@Override
	public int getRowCount() {
		return this.board.length;
	}

	@Override
	public int getColumnCount() {
		return this.board[0].length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
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
