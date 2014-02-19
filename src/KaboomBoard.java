import java.util.LinkedList;
import java.util.Random;

import javax.swing.table.AbstractTableModel;


public class KaboomBoard extends AbstractTableModel {

	private KaboomCell[][] board;
	private int boardSize, difficulty, boardNum, numBombs;
	private String[] columnNames;
	
	public KaboomBoard(int boardSize, int difficulty, int boardNum)
	{
		this.boardSize = boardSize;
		this.difficulty = difficulty;
		this.boardNum = boardNum;

		this.generateBoard();
		
		this.columnNames = new String[boardSize];
		
        for(String str : this.columnNames)
        {
            str = "";
        }
	}
	
	private void generateBoard()
	{
		int numGeneratedBombs = (this.boardSize * this.boardSize) / this.difficulty;
		Random generator = new Random(boardNum);
		
		this.board = new KaboomCell[boardSize][boardSize];
		
		for(int iter = 0; iter < numGeneratedBombs; iter++)
		{
			int curRow = generator.nextInt(boardSize);
			int curCol = generator.nextInt(boardSize);
			
			if(board[curRow][curCol] == null)
			{
				board[curRow][curCol] = new KaboomCell(KaboomPieces.bomb, curRow, curCol);
				this.numBombs++;
			}
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
	
	public int getNumBombs()
	{
		return this.numBombs;
	}
	
	private void setCellState(int row, int col)
	{
		board[row][col] = new KaboomCell(KaboomPieces.empty, row, col);
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
		if(rDir != 0 || cDir != 0)
		{
			if(row + rDir >= 0 && row + rDir < board.length)
			{
				if(col + cDir >= 0 && col + cDir < board[0].length)
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
		if(rDir != 0 || cDir != 0)
		{
			if(row + rDir >= 0 && row + rDir < board.length)
			{
				if(col + cDir >= 0 && col + cDir < board[0].length)
				{
//					if(board[row + rDir][col + cDir] != null && board[row + rDir][col + cDir].getCellState() == KaboomPieces.covered && board[row + rDir][col + cDir].getNumBombsNear() == 0) {
//						queue.add((KaboomCell)board[row + rDir][col + cDir]);
//					}
					
					if(board[row + rDir][col + cDir] != null && board[row + rDir][col + cDir].getCellState() == KaboomPieces.covered && !board[row + rDir][col + cDir].isBomb())
					{
						if(board[row + rDir][col + cDir].getNumBombsNear() == 0)
						{
							queue.add((KaboomCell)board[row + rDir][col + cDir]);
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
					checkForEmptyNeighbors(curCell.getRow(), curCell.getColumn(), rDir, cDir, queue);
				}
			}
			
//			checkForEmptyNeighbors(curCell.getRow(), curCell.getColumn(), 1, 0, queue);
//			checkForEmptyNeighbors(curCell.getRow(), curCell.getColumn(), -1, 0, queue);
//			checkForEmptyNeighbors(curCell.getRow(), curCell.getColumn(), 0, 1, queue);
//			checkForEmptyNeighbors(curCell.getRow(), curCell.getColumn(), 0, -1, queue);
		}
	}
	
	public void createCheatBoard()
	{
		for(int rowIter = 0; rowIter < board.length; rowIter++)
		{
			for(int colIter = 0; colIter < board[0].length; colIter++)
			{
				board[rowIter][colIter] = new KaboomCell(KaboomPieces.empty, rowIter, colIter);
				board[rowIter][colIter].setUncovered();
			}
		}
		
		board[0][0] = new KaboomCell(KaboomPieces.bomb, 0, 0);
		board[0][1] = new KaboomCell(KaboomPieces.empty, 0, 1);
		board[0][1].setNumBombsNear(1);
	}
	
	public void setToPeak()
	{
		for(int rowIter = 0; rowIter < board.length; rowIter++)
		{
			for(int colIter = 0; colIter < board[0].length; colIter++)
			{
				board[rowIter][colIter].setUncovered();
			}
		}
	}
	
	public boolean boardIsCleared()
	{
		boolean boardCleared = true;
		
		for(int rowIter = 0; rowIter < board.length && boardCleared; rowIter++)
		{
			for(int colIter = 0; colIter < board[0].length && boardCleared; colIter++)
			{
				if(board[rowIter][colIter].getCellState() == KaboomPieces.covered && !board[rowIter][colIter].isBomb())
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
