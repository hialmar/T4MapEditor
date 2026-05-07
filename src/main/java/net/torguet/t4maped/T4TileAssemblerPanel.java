package net.torguet.t4maped;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static net.torguet.t4maped.T4DrawingPanel.CELL_SIZE;

public class T4TileAssemblerPanel  extends JPanel {
    private final T4DrawingPanel drawingPanel;

    private int currentTile;

    private int currentTopLeftSubTile;
    private int currentTopRightSubTile;
    private int currentBottomLeftSubTile;
    private int currentBottomRightSubTile;

    private int selectedSubTile;

    private int currentSubTile;

    private static final int zoom = 8;

    public T4TileAssemblerPanel(T4DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(CELL_SIZE*8+10, CELL_SIZE*16+10));
        setBackground(Color.GRAY);
    }

    public void mousePressed(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / CELL_SIZE*2;
        i = (evt.getX() - 5) / CELL_SIZE*2;
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        repaint();
    }



    public void mouseReleased(MouseEvent evt) {
    }

    public void refresh() {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawingPanel.drawTile(g, 5, 5, currentTile, zoom);

        // draw a red empty rectangle
        g.setColor(Color.RED);
        if (selectedSubTile != -1) {
            g.drawRect(5 + selectedSubTile&0x2*zoom*CELL_SIZE, 5 + selectedSubTile&0x1*selectedSubTile*zoom*CELL_SIZE, zoom*CELL_SIZE - 1, zoom*CELL_SIZE - 1);
        }
    }


    public void mouseEntered(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / CELL_SIZE;
        i = (evt.getX() - 5) / CELL_SIZE;
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int val = i*20 + j;
        if (val < nbTuiles)
            this.setToolTipText(String.valueOf(val));
    }

    public void mouseMoved(MouseEvent e) {
        mouseEntered(e);
    }

    public void previousTile() {
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        currentTile--;
        if (currentTile<0)
            currentTile = nbTuiles-1;
        if (currentTile < 0)
            currentTile = 0;
        repaint();
    }

    public void nextTile() {
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        currentTile++;
        if (currentTile>=nbTuiles)
            currentTile = 0;
        repaint();
    }

    public void previousSubTile() {
    }

    public void nextSubTile() {
    }
}
