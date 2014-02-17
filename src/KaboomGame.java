import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Random;

import javax.swing.Timer;


public class KaboomGame extends Observable {

	private int boardSize, difficulty, boardNum, numMoves, flagCount, timeSeconds;
	private KaboomBoard board;
	private Timer timer;
	private static final int kMillisecondsPerSecond = 1000;
	
	public KaboomGame(int boardSize, int difficulty, int boardNum) {
		this.boardSize = boardSize;
		this.difficulty = difficulty;
		this.boardNum = boardNum;
		this.numMoves = 0;
		this.flagCount = 0;
		this.timeSeconds = 0;
		
		this.board = new KaboomBoard(this.boardSize, this.difficulty, this.boardNum);
		
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
	}
	
	public void takeTurn(int row, int col)
	{
		if(row > 0 && row < board.getRowCount() && col > 0 && col < board.getColumnCount() && board.getValueAt(row, col) == KaboomPieces.covered)
		{
			this.numMoves++;
			KaboomCell chosenCell = (KaboomCell)board.getValueAt(row, col);
			chosenCell.setUncovered();
			if(chosenCell.isBomb())
			{
				this.timer.stop();
				//TO DO: Signal game over
			}
			else
			{
				if(chosenCell.getNumBombsNear() == 0)
				{
					this.board.uncoverNeighboringEmptyCells(row, col);
				}
			}
		}
	}
	
	public void toggleFlag(int row, int col) {
		if(row > 0 && row < board.getRowCount() && col > 0 && col < board.getColumnCount())
		{
			KaboomCell flaggedCell = ((KaboomCell)this.board.getValueAt(row, col));
			if(flaggedCell.getCellState() == KaboomPieces.covered)
			{
				this.flagCount++;
				flaggedCell.setCellState(KaboomPieces.flagged);
			}
			else if(flaggedCell.getCellState() == KaboomPieces.flagged)
			{
				this.flagCount--;
				flaggedCell.setCellState(KaboomPieces.covered);
			}
			
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void cheat()
	{
		this.board.createCheatBoard();
		
		this.setChanged();
		this.notifyObservers();
	}
	
}
