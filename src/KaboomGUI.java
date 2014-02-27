import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

/**
 * GUI version of the Kaboom Game.
 * 
 * @author erikowen
 * @version 1
 *
 */
public class KaboomGUI extends JFrame implements Observer {

	private JLabel gameStatus = null;
	private JMenuBar menuBar;
	private JTable table;
	private JPanel statusPane;
	private KaboomBoard model;
	private ImageIcon [] images = new ImageIcon[10];
	private int boardNum, boardPrefSize, boardDifficulty;
	private Preferences prefs;
	private HighScores hallOfFame;
	private KaboomGame game;
	private int kTileWidth = 65;
	private int kTileHeight = 43;
	private static final int kNumBoards = 5000, kSecondsInAMin = 60,
			kMaxNameLength = 20, kHiddenEraseHighScores = 0;

	public KaboomGUI() throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		this.boardNum = new Random().nextInt(kNumBoards);
		this.prefs = new Preferences();
		this.hallOfFame = new HighScores();
		this.boardPrefSize = prefs.getDefaultBoardSize();
		this.boardDifficulty = prefs.getDefaultDifficulty();

		this.game = new KaboomGame(this.boardPrefSize, this.boardDifficulty,
				this.boardNum);

		UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName());

		super.setTitle("Kaboom - board " + this.boardNum);

		this.game.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		Integer response = (Integer) arg;

		String timerString = "" + game.getTimerCount() / kSecondsInAMin + 
				":" + String.format("%02d", game.getTimerCount() 
						% kSecondsInAMin);
		gameStatus = new JLabel("Moves: " + game.getMoveCount() +
				"      " + "Flags: " + game.getFlagCount() + "/" + 
				game.getNumBombs() + "      " + timerString);
		this.statusPane.removeAll();
		this.statusPane.add(gameStatus);
		this.statusPane.revalidate();
		this.statusPane.repaint();

		if(response != null && response == KaboomGame.kUpdateBoardLoss)
		{
			//TODO
			JOptionPane.showMessageDialog(
					null,
					"You lost.",
					"Game Over",
					JOptionPane.INFORMATION_MESSAGE
					);
		}
		/*Determines if the player has won*/
		else if(response != null && response == KaboomGame.kUpdateBoardWin)
		{
			//TODO
			/*GAME WON DIALOG POPS UP*/
			String choice = JOptionPane.showInputDialog(
					this,
					"Game " + this.boardNum + " Cleared!\nSave your time of " + 
							timerString + "? (y/n)",
							"Game Won Notification",
							JOptionPane.QUESTION_MESSAGE
					);
			/*If the user entered the letter 'y'*/
			if(choice != null && choice.toLowerCase().trim().equals("y"))
			{
				String name = JOptionPane.showInputDialog(
						this,
						"Your score of " + timerString +
						" will be entered into the Hall of Fame.\nEnter your name:",
						"Name Entry",
						JOptionPane.QUESTION_MESSAGE
						);
				/*If the pop up dialogue was executed correctly*/
				if(name != null)
				{
					try
					{
						this.hallOfFame.addHighScore(name, game.getTimerCount());
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	/** Place all the Swing widgets in the frame of this GUI.
	 * @post the GUI is visible.  
	 */
	public void run()
	{
		loadImages();
		statusPane = new JPanel();
		gameStatus = new JLabel("");
		statusPane.add(gameStatus);
		statusPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		getContentPane().add(statusPane);
		this.game.newGame(boardPrefSize, boardDifficulty, boardNum);
		this.model = this.game.getBoard();
		this.table = new JTable(model)
		{
			private String file = "icons/" + "background.png";
			private URL backgroundUrl = getClass().getResource(file);
			private ImageIcon backgroundOrig = new ImageIcon(backgroundUrl);
			private Image scaled = backgroundOrig.getImage().getScaledInstance(
					images[0].getIconWidth() * boardPrefSize, images[0].getIconHeight()
					* boardPrefSize, Image.SCALE_DEFAULT);
			//private ImageIcon backgroundIcon = new ImageIcon(scaled);
			private ImageIcon backgroundIcon = new ImageIcon(backgroundOrig.getImage());

			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int column)
			{
				Component comp = super.prepareRenderer(renderer, row, column);
				/*If the component is of type JComponent, setOpaque to false*/
				if( comp instanceof JComponent )
				{   
					JComponent component = (JComponent)comp;
					component.setOpaque(false);
					component.setForeground(Color.WHITE);
					component.setFont(component.getFont().deriveFont(32.0f));

				}

				return comp;
			}

			//String url = "https://mediacru.sh/DJPai-Ixkvfp.jpg";
			// Be patient, takes about ten seconds to load the URL
			//ImageIcon background = new ImageIcon(ImageIO.read(new URL(url)));
			// Override paint so as to show the table background
			public void paint(Graphics g)
			{
				g.drawImage(backgroundIcon.getImage(), 0, 0, null, null);
				// Now let the paint do its usual work
				super.paint(g);
			}
		};

		table.setDefaultEditor(Object.class, null);
		table.setDefaultRenderer(KaboomCell.class, new KaboomCellRenderer(images));
		//Define the layout manager that will control order of components
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		//Add a menubar
		menuBar = new javax.swing.JMenuBar();
		JMenu mnuGame = new JMenu("Game");
		menuBar.add(mnuGame);

		JMenuItem mnuRestart = new JMenuItem("Restart");
		mnuRestart.setAccelerator(KeyStroke.getKeyStroke('R', ActionEvent.ALT_MASK));
		mnuRestart.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				game.restart();
				newGame();
			}
		});
		mnuGame.add(mnuRestart);

		JMenuItem mnuNew = new JMenuItem("New");
		mnuNew.setAccelerator(KeyStroke.getKeyStroke('N', ActionEvent.ALT_MASK));
		mnuNew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				/*If board number is 5000 then wrap around to board 1*/
				if(boardNum == kNumBoards)
				{
					boardNum = 1;
				}
				else
				{
					boardNum++;
				}
				
				game.newGame(boardPrefSize, boardDifficulty, boardNum);
				newGame();
			}
		});
		mnuGame.add(mnuNew);

		JMenuItem mnuSelectGame = new JMenuItem("Select");
		mnuSelectGame.setAccelerator(KeyStroke.getKeyStroke('G', ActionEvent.ALT_MASK));
		mnuSelectGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String gameNumString = JOptionPane.showInputDialog(
						null,
						"Enter desired game number (1 - 5000)",
						"Select Game",
						JOptionPane.QUESTION_MESSAGE
						);

				/*If the ganeNumString is not null then change to that game number*/
				if(gameNumString != null)
				{
					try
					{
						int gameNum = Integer.parseInt(gameNumString);
						boardNum = gameNum;
						game.newGame(boardPrefSize, boardDifficulty, boardNum);
						newGame();
					}
					catch(NumberFormatException nfe)
					{
						boardNum = boardNum;
					}
				}          
			}
		});
		mnuGame.add(mnuSelectGame);

		JMenuItem mnuScores = new JMenuItem("Scores");
		mnuScores.setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.ALT_MASK));
		mnuScores.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(
						null,
						hallOfFame.getHighScores(),
						"Hall of Fame",
						JOptionPane.PLAIN_MESSAGE
						);

			}

		});
		mnuGame.add(mnuScores);

		JMenuItem mnuPeek = new JMenuItem("Peek");
		mnuPeek.setAccelerator(KeyStroke.getKeyStroke('P', ActionEvent.ALT_MASK));
		mnuPeek.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				game.peek();
				repaint();
			}
		});
		mnuGame.add(mnuPeek);
		
		JMenuItem mnuCheat = new JMenuItem("Cheat");
		mnuCheat.setAccelerator(KeyStroke.getKeyStroke('C', ActionEvent.ALT_MASK));
		mnuCheat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				game.cheat();
				repaint();
			}
		});
		mnuGame.add(mnuCheat);

		JMenuItem mnuAbout = new JMenuItem("About");
		mnuAbout.setAccelerator(KeyStroke.getKeyStroke('A', ActionEvent.ALT_MASK));
		mnuAbout.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(
						null,
						"Kaboom Game by Erik Owen",
						"About",
						JOptionPane.INFORMATION_MESSAGE
						);
			}
		});
		mnuGame.add(mnuAbout);
		
		JMenuItem mnuQuit = new JMenuItem("Quit");
		mnuQuit.setAccelerator(KeyStroke.getKeyStroke('Q', ActionEvent.ALT_MASK));
		mnuQuit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		mnuGame.add(mnuQuit);
		setJMenuBar(menuBar);

		// Define the characteristics of the table that shows the game board        
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(false);
		table.setRowHeight(kTileHeight);

		table.setOpaque(false);
		table.setShowGrid(false);

		table.setAlignmentX(Component.CENTER_ALIGNMENT);
		getContentPane().add(table);

		// Define the mouse listener that will handle player's clicks.
		table.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent ev)
			{
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();
				// Is it a left mouse click?
				if (SwingUtilities.isLeftMouseButton(ev)) 
				{
					game.takeTurn(row, col);
				}
				else if(SwingUtilities.isRightMouseButton(ev))
				{
					row = (int) (ev.getPoint().getY()/kTileHeight);
					col = (int) (ev.getPoint().getX()/kTileWidth);
					game.toggleFlag(row, col);
				}

				System.out.println("Row: " + row);
				System.out.println("Column: " + col);
				repaint();
			}
		}
				);

		table.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				int keyCode = e.getKeyCode();
				/*If the user hits the space bar*/
				if (keyCode == KeyEvent.VK_SPACE)
				{
					int col = table.getSelectedColumn();
					int row = table.getSelectedRow();
					game.takeTurn(row, col);
				}
				repaint();
			}
		});

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setResizable(false);
		this.pack();
		this.setVisible(true);

	}

	protected void loadImages()
	{
		KaboomPieces [] lights = KaboomPieces.values();

		// Any images will be loaded here
		for (int ndx = 0; ndx < lights.length - 1; ndx++)
		{
			String file = "icons/" + lights[ndx].name().toLowerCase() + ".png";
			URL url = getClass().getResource(file);
			//System.out.println(url.getPath());
			ImageIcon original = new ImageIcon(url);
			Image scaled = original.getImage().getScaledInstance(kTileWidth,
					kTileHeight, Image.SCALE_DEFAULT);
			ImageIcon icon = new ImageIcon(scaled);
			images[ndx] = icon;
		}
	}

	/**
	 * Start a new game by putting new values in the board
	 */
	private void newGame()
	{
		model = game.getBoard();
		table.setModel(model);
		setTitle("Kaboom - board " + boardNum);
	}

	/**
	 * Driver method used to run the CollapseGUI.
	 * 
	 * @param args the parameters to run this class
	 */
	public static void main(String[] args)
	{
		// Create the GUI 
		KaboomGUI frame;
		try
		{
			frame = new KaboomGUI();
			frame.run();   // do the layout of widgets
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}