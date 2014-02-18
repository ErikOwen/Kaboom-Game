import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Random;

import javax.swing.Timer;


public class KaboomGame extends Observable {

	private int boardSize, difficulty, boardNum, numMoves, flagCount, timeSeconds;
	private KaboomBoard board;
	private Timer timer;
	private boolean gameOver, gameWon;
	private static final int kMillisecondsPerSecond = 1000;
	
	public KaboomGame(int boardSize, int difficulty, int boardNum) {
		this.boardSize = boardSize;
		this.difficulty = difficulty;
		this.boardNum = boardNum;
		this.numMoves = 0;
		this.flagCount = 0;
		this.timeSeconds = 0;
		this.gameOver = false;
		this.gameWon = false;
		
        ActionListener timerListener = new ActionListener()
        {   
			@Override
			public void actionPerformed(ActionEvent e) {
				timeSeconds++;
			}
        };
		
		this.timer = new Timer(kMillisecondsPerSecond, timerListener);
		this.timer.setInitialDelay(0);
		this.timer.start();
		
		//newGame(this.boardSize, this.difficulty, this.boardNum);
	}
	
	public void newGame(int boardSize, int difficulty, int boardNum)
	{
		this.boardSize = boardSize;
		this.difficulty = difficulty;
		this.boardNum = boardNum;
		
		this.numMoves = 0;
		this.flagCount = 0;
		this.timeSeconds = 0;
		this.gameOver = false;
		this.gameWon = false;
		this.board = new KaboomBoard(this.boardSize, this.difficulty, this.boardNum);
		this.timer.restart();
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void restart()
	{
		newGame(this.boardSize, this.difficulty, this.boardNum);
	}
	
	public void takeTurn(int row, int col)
	{
		if(row >= 0 && row < board.getRowCount() && col >= 0 && col < board.getColumnCount() && ((KaboomCell)board.getValueAt(row, col)).getCellState() == KaboomPieces.covered)
		{
			this.numMoves++;
			KaboomCell chosenCell = (KaboomCell)board.getValueAt(row, col);
			chosenCell.setUncovered();
			if(chosenCell.isBomb())
			{
				this.timer.stop();
				this.gameOver = true;
				chosenCell.setCellState(KaboomPieces.bombHit);
				this.board.setToPeak();
			}
			else
			{
				if(chosenCell.getNumBombsNear() == 0)
				{
					this.board.uncoverNeighboringEmptyCells(row, col);
				}
				
				this.gameWon = board.boardIsCleared();
				if(this.gameWon)
				{
					this.gameOver = true;
				}
			}
			
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public boolean isGameOver()
	{
		return this.gameOver;
	}
	
	public KaboomBoard getBoard()
	{
		return this.board;
	}
	
	public int getMoveCount()
	{
		return this.numMoves;
	}
	
	public int getFlagCount()
	{
		return this.flagCount;
	}
	
	public int getNumBombs()
	{
		return this.board.getNumBombs();
	}
	
	public boolean gameWon()
	{
		return this.gameWon;
	}
	
	public int getTimerCount()
	{
		return this.timeSeconds;
	}
	
	public void toggleFlag(int row, int col) {
		KaboomCell flaggedCell = ((KaboomCell)this.board.getValueAt(row, col));
		
		if(flaggedCell.getCellState() == KaboomPieces.covered)
		{
			this.flagCount++;
			flaggedCell.setCellState(KaboomPieces.flagged);
			flaggedCell.setUncovered();
		}
		else if(flaggedCell.getCellState() == KaboomPieces.flagged)
		{
			this.flagCount--;
			flaggedCell.setCovered();
		}
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void peek()
	{
		this.board.setToPeak();
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void cheat()
	{
		this.board.createCheatBoard();
		
		this.setChanged();
		this.notifyObservers();
	}
	
}
