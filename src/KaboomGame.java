import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.Timer;


public class KaboomGame extends Observable {

	private int boardSize, difficulty, boardNum, numMoves, flagCount, timeSeconds, winCount;
	private KaboomBoard board;
	private Timer timer;
	private boolean hasLost, hasWon, hitBomb;
	private static final int kMillisecondsPerSecond = 1000;
	public static final int kUpdateBoardLoss = 1, kUpdateBoardWin = 2;
	
	public KaboomGame(int boardSize, int difficulty, int boardNum) {
		this.boardSize = boardSize;
		this.difficulty = difficulty;
		this.boardNum = boardNum;
		this.numMoves = 0;
		this.flagCount = 0;
		this.timeSeconds = 0;
		this.winCount = 0;
		this.hasLost = false;
		this.hasWon = false;
		this.hitBomb = false;
		
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
	
	public void newGame(int boardSize, int difficulty, int boardNum)
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
		this.hitBomb = false;
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
		if(row >= 0 && row < board.getRowCount() && col >= 0 && col < board.getColumnCount() /*&& ((KaboomCell)board.getValueAt(row, col)).getCellState() == KaboomPieces.covered*/)
		{
			this.numMoves++;
			KaboomCell chosenCell = (KaboomCell)board.getValueAt(row, col);
			chosenCell.setUncovered();
			
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
				if(chosenCell.getNumBombsNear() == 0)
				{
					this.board.uncoverNeighboringEmptyCells(row, col);
				}
				
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
	
//	public boolean gameAlreadyWon()
//	{
//		return this.winCount >= 1;
//	}
	
//	public void increaseWinCount()
//	{
//		this.winCount++;
//	}
	
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
	
	public boolean hitBomb()
	{
		return this.hitBomb;
	}
	
//	public boolean gameWon()
//	{
//		return this.hasWon;
//	}
	
//	public boolean gameLost()
//	{
//		return this.hasLost;
//	}
	
	public int getTimerCount()
	{
		return this.timeSeconds;
	}
	
	public void toggleFlag(int row, int col) {
		KaboomCell flaggedCell = ((KaboomCell)this.board.getValueAt(row, col));
		
		if(!flaggedCell.isFlagged())
		{
			this.flagCount++;
			//flaggedCell.setCellState(KaboomPieces.flagged);
			flaggedCell.setFlagged();
			//flaggedCell.setUncovered();
		}
		else/* if(flaggedCell.getCellState() == KaboomPieces.flagged)*/
		{
			this.flagCount--;
			flaggedCell.setUnflagged();
			//flaggedCell.setCovered();
		}
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void peek()
	{
		this.board.setToPeak();
		//this.isPeeking = true;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void cheat()
	{
		//this.isPeeking = false;
		//this.gameOver = false;
		this.board.createCheatBoard();
		
		this.setChanged();
		this.notifyObservers();
	}
	
}
