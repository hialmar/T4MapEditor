package net.torguet.t4maped;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static net.torguet.t4maped.T4DrawingPanel.CELL_SIZE;

public class T4TileAssemblerPanel  extends JPanel {
    private final T4DrawingPanel drawingPanel;

    private int currentTile;

    private final int[] currentSubTiles = new int[4];

    private int selectedSubTile;

    private int currentSubTile;

    private static final int zoom = 8;

    public T4TileAssemblerPanel(T4DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(CELL_SIZE*8+10, CELL_SIZE*20+10));
        setBackground(Color.BLACK);
    }

    public void mousePressed(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / (zoom/2*CELL_SIZE);
        i = (evt.getX() - 5) / (zoom/2*CELL_SIZE);
        selectedSubTile = i+2*j;
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
            g.drawRect(5 + (selectedSubTile&0x1)*(zoom/2*CELL_SIZE),
                    5 + (selectedSubTile&0x2)/2*(zoom/2*CELL_SIZE),
                    zoom/2*CELL_SIZE - 1, zoom/2*CELL_SIZE - 1);
        }
    }


    public void mouseEntered(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / (zoom/2*CELL_SIZE);
        i = (evt.getX() - 5) / (zoom/2*CELL_SIZE);
        int val = i+2*j;
        if (val >= 0 && val <= 3)
            this.setToolTipText("Tile "+currentTile+" SubTile "+currentSubTiles[val]);
        else
            this.setToolTipText("Tile "+currentTile+" No SubTile");
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
        computeSubTiles();
    }

    public void nextTile() {
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        currentTile++;
        if (currentTile>=nbTuiles)
            currentTile = 0;
        repaint();
        computeSubTiles();
    }

    public void previousSubTile() {
    }

    public void nextSubTile() {
    }

    private void computeSubTiles() {
        if (drawingPanel.getNbTuiles()>4) {
            for (int i = 0; i < 4; i++)
                currentSubTiles[i] = drawingPanel.getTuiles()[currentTile * 4 + i];
        }
    }

}
