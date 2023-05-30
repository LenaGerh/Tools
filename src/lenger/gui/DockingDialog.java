package lenger.gui;

import javax.swing.JDialog;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Dimension;

public class DockingDialog extends JDialog implements ComponentListener{

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    public static final int NORTH = 0, WEST = 1, EAST = 2, SOUTH = 3;
    public static final int TOP = 0, LEFT = 1, RIGHT = 2, BOTTOM = 3;

    private Double extendPercent = .1;
    private int position;
    private boolean shouldBeVisible;

    public DockingDialog(Frame parent, String title, int position){
        super(parent, title);

        if(parent == null)
            throw new IllegalArgumentException("parameter parent cannot be null");

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

    public void showWin(){
        this.shouldBeVisible = true;
        this.setVisible(true);
        this.getParent().revalidate();
    }

    public void hideWin(){
        shouldBeVisible = false;
        this.setVisible(false);
        this.getParent().revalidate();
    } 

    private void move(){
        switch(this.position){
            default -> {
                this.setSize(new Dimension(this.getParent().getWidth(), (int)(this.getParent().getHeight() * this.extendPercent)));
                this.setLocation(this.getParent().getLocation().x,-this.getHeight() + this.getParent().getLocation().y);
            }
            case SOUTH -> {
                this.setLocation(this.getParent().getLocation().x, this.getParent().getHeight() + this.getParent().getLocation().y);
                this.setSize(new Dimension(this.getParent().getWidth(), (int)(this.getParent().getHeight() * this.extendPercent)));
            }
            case WEST -> {
                this.setSize(new Dimension((int)(this.getParent().getWidth() * this.extendPercent), this.getParent().getHeight()));
                this.setLocation(-this.getWidth() + this.getParent().getLocation().x, this.getParent().getLocation().y);
            }
            case EAST -> {
                this.setLocation(this.getParent().getLocation().x + this.getParent().getWidth(), this.getParent().getLocation().y);
                this.setSize(new Dimension((int)(this.getParent().getWidth() * this.extendPercent),this.getParent().getHeight()));
            }
        }

        this.getContentPane().setPreferredSize(this.getSize());
    }

    //region setters
    public void setExtendPercent(Double extPerc){
        this.extendPercent = extPerc / 100;
    }
    //endregion

    //region getters
    public boolean isOutOfBounds(){
        return this.getLocationOnScreen().x < 0 || this.getLocationOnScreen().y < 0 ||
               this.getLocationOnScreen().x + this.getWidth() > screenSize.getWidth() ||
               this.getLocationOnScreen().y + this.getHeight() > screenSize.getHeight();
    }

    public boolean isInBounds(){
        return !this.isOutOfBounds();
    }
    //endregion

    //region ComponentListener
    @Override
    public void componentHidden(ComponentEvent arg0) {
        if(arg0.getSource() == this.getParent() && shouldBeVisible && this.isVisible())
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
        if(arg0.getSource() == this.getParent() && shouldBeVisible && !this.isVisible())
            this.setVisible(true);

        this.getParent().revalidate();
    }
    //endregion

}
