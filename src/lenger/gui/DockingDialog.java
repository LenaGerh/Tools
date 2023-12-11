package lenger.gui;

import javax.swing.JDialog;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Dimension;


/**
 * A dialog following its parent on one of its four Sides
 * @author Lenardt Gerhardts
 * @since 17.0.1
 */
public class DockingDialog extends JDialog implements ComponentListener{

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    public static final int NORTH = 0, WEST = 1, EAST = 2, SOUTH = 3;
    public static final int TOP = 0, LEFT = 1, RIGHT = 2, BOTTOM = 3;
    public static final Double TEN_PERCENT = .1;

    private Double extendPercent = TEN_PERCENT;
    private int position;

    /**
     * <p>Creates a JDialog that sticks to his parent</p>
     * <p>will throw a {@code IllegalArgumentException} when {@code parent == null}</p>
     * <p>position can be one of {@code NORTH | WEST | EAST | SOUTH} or {@code TOP | LEFT | RIGHT | BOTTOM}</p>
     * @param parent
     * @param title
     * @param position
     * @author Lenardt Gerhardts
     * @since 17.0.1
     */
    public DockingDialog(Frame parent, String title, int position){
        super(parent, title);

        if(parent == null)
            throw new IllegalArgumentException("parameter \"parent\" of JDialog:%s cannot be null".formatted(this.hashCode()));

        this.setVisible(false);
        
        this.position = position;
        if(position < 0 || position > 3)
            this.position = 0;
        
        this.getParent().addComponentListener(this);
        this.addComponentListener(this);
        
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
    }

    /**
     * Changes The visibility of the Dialog based on the parameter {@code visible}.
     * 
     * <p>Adds {@code getParent().revalidate()} incase you want to switch the contents between tha Dialog and the Parent.</p>
     * 
     * @author Lenardt Gerhardts
     * @since 17.0.1
     * @param visible
     */
    @Override
    public void setVisible(boolean visible){
        super.setVisible(visible);
        this.getParent().revalidate();
    }

    /**
     * Moves the Dialog based on the parents Position and Width/Height
     * 
     * @author Lenardt Gerhardts
     * @since 17.0.1
     */
    private void move(){
        switch(this.position){
            default: {
                this.setSize(new Dimension(this.getParent().getWidth(), (int)(this.getParent().getHeight() * this.extendPercent)));
                this.setLocation(this.getParent().getLocation().x,-this.getHeight() + this.getParent().getLocation().y);
                break;
            }
            case SOUTH: {
                this.setLocation(this.getParent().getLocation().x, this.getParent().getHeight() + this.getParent().getLocation().y);
                this.setSize(new Dimension(this.getParent().getWidth(), (int)(this.getParent().getHeight() * this.extendPercent)));
                break;
            }
            case WEST: {
                this.setSize(new Dimension((int)(this.getParent().getWidth() * this.extendPercent), this.getParent().getHeight()));
                this.setLocation(-this.getWidth() + this.getParent().getLocation().x, this.getParent().getLocation().y);
                break;
            }
            case EAST : {
                this.setLocation(this.getParent().getLocation().x + this.getParent().getWidth(), this.getParent().getLocation().y);
                this.setSize(new Dimension((int)(this.getParent().getWidth() * this.extendPercent),this.getParent().getHeight()));
                break;
            }
        }

        this.getContentPane().setPreferredSize(this.getSize());
    }

    //region setters

    /**
     * {@code extPerc} sets how much the Dialog should extend in Width | Height based on the parent Window, in Percent.
     * @author Lenardt Gerhardts
     * @since 17.0.1
     * @param extPerc
     */
    public void setExtendPercent(Double extPerc){
        this.extendPercent = extPerc / 100;
    }

    //endregion

    //region getters

    /**
     * Returns {@code true} if any of the Dialog borders touch the Screen borders
     * @return boolean
     * @author Lenardt Gerhardts
     * @since 17.0.1
     */
    public boolean isOutOfBounds(){
        return this.getLocationOnScreen().x < 0 || this.getLocationOnScreen().y < 0 ||
               this.getLocationOnScreen().x + this.getWidth() > screenSize.getWidth() ||
               this.getLocationOnScreen().y + this.getHeight() > screenSize.getHeight();
    }

    /**
     * Simple inversion of {@link #isOutOfBounds() isOutOfBounds} for readability
     * @return boolean
     * @author Lenardt Gerhardts
     * @since 17.0.1
     */
    public boolean isInBounds(){
        return !this.isOutOfBounds();
    }

    //endregion

    //region ComponentListener
    @Override
    public void componentHidden(ComponentEvent arg0) {
        if(arg0.getSource() == this.getParent() && this.isVisible() && this.isVisible())
            this.setVisible(false);
    }
    @Override
    public void componentMoved(ComponentEvent arg0) {
        move();
    }
    @Override
    public void componentResized(ComponentEvent arg0) {
        if(arg0.getSource() == this.getParent())
            move();
    }
    @Override
    public void componentShown(ComponentEvent arg0) {
        if(arg0.getSource() == this.getParent() && this.isVisible() && !this.isVisible())
            this.setVisible(true);

        this.getParent().revalidate();
    }
    //endregion

}
