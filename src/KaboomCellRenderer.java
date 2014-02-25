import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Renderer class for a cell in the Collapse Game.
 * 
 * @author Erik Owen
 * @version 1
 */
public class KaboomCellRenderer extends DefaultTableCellRenderer
{
    //private static final int kGreen = 0, kPurple = 1, kRed = 2;
    private static final int kCovered = 0, kFlagged = 1, kBomb = 2, kBombHit = 3;
    private ImageIcon [] images;
    
    /**
     * Constructor for the GameCellRenderer
     * 
     * @param images an array of imageIcons
     */
    public KaboomCellRenderer(ImageIcon [] images)
    {
        this.images = images;
    }
    
    /**
     * Sets the value of a specified cell.
     * 
     * @param  value the value to be set
     */
    public void setValue(Object value)
    {
    	KaboomCell curCell = (KaboomCell) value;
    	KaboomPieces piece = curCell.getCellState();
        //KaboomPieces piece = (KaboomPieces) value;
        
        setIcon(null);
        setText(null);
        
        if(piece == KaboomPieces.covered)
        {
            setIcon(images[kCovered]);
        }
        else if(piece == KaboomPieces.flagged)
        {
            setIcon(images[kFlagged]);
        }
        else if(piece == KaboomPieces.bomb)
        {
            setIcon(images[kBomb]);
        }
        else if(piece == KaboomPieces.bombHit)
        {
            setIcon(images[kBombHit]);
        }

    }
}
