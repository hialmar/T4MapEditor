package net.torguet.t4maped;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static net.torguet.t4maped.T4DrawingPanel.CELL_SIZE;


public class T4PalettePanel extends JPanel {
    private final T4DrawingPanel drawingPanel;

    private static final int numberOfVerticalTiles = 10;
    private static final int numberOfHorizontalTiles = 15;
    private static final int zoom = 2;

    private int selectedTile = -1;

    public T4PalettePanel(T4DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(CELL_SIZE*zoom*numberOfHorizontalTiles+10,
                CELL_SIZE*zoom*numberOfVerticalTiles+20));
        setBackground(Color.BLACK);
    }

    public void mousePressed(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / (CELL_SIZE*zoom);
        i = (evt.getX() - 5) / (CELL_SIZE*zoom);
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int val = i*numberOfVerticalTiles + j;
        if (val < nbTuiles) {
            selectedTile = val;
            drawingPanel.setCurrentValue(val);
        } else {
            selectedTile = -1;
            drawingPanel.selectMode();
        }
        repaint();
    }



    public void mouseReleased() {
    }

    public void refresh() {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int j = 0;
        int i = 0;
        for(; i< nbTuiles; i++) {
            drawingPanel.drawTile(g, 5+j* CELL_SIZE*zoom, 5+(i%numberOfVerticalTiles)* CELL_SIZE*zoom, i, 0.8f*zoom);
            if (i == selectedTile) {
                g.setColor(Color.RED);
                g.drawRect(5+j* CELL_SIZE*zoom, 5+(i%numberOfVerticalTiles)* CELL_SIZE*zoom,
                        (int)(0.8f*CELL_SIZE*zoom-1),  (int)(0.8f*CELL_SIZE*zoom-1));
            }
            if ((i%numberOfVerticalTiles) == numberOfVerticalTiles-1) {
                j++;
            }
        }
        // draw a red empty rectangle
        g.setColor(Color.RED);
        g.drawRect(5+j* CELL_SIZE*zoom, 5+(i%numberOfVerticalTiles)* CELL_SIZE*zoom, CELL_SIZE*zoom-1, CELL_SIZE*zoom-1);
    }


    public void mouseEntered(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / (CELL_SIZE*zoom);
        i = (evt.getX() - 5) / (CELL_SIZE*zoom);
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int val = i*numberOfVerticalTiles + j;
        if (val < nbTuiles)
            this.setToolTipText(val +" "+drawingPanel.getCommentaireTuile()[val]);
    }

    public void mouseMoved(MouseEvent e) {
        mouseEntered(e);
    }
}
