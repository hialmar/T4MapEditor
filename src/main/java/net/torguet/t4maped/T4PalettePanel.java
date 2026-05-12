package net.torguet.t4maped;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static net.torguet.t4maped.T4DrawingPanel.CELL_SIZE;


public class T4PalettePanel extends JPanel {
    private final T4DrawingPanel drawingPanel;

    public T4PalettePanel(T4DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(CELL_SIZE*2*10+10, CELL_SIZE*2*15+10));
        setBackground(Color.BLACK);
    }

    public void mousePressed(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / (CELL_SIZE*2);
        i = (evt.getX() - 5) / (CELL_SIZE*2);
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int val = i*15 + j;
        if (val < nbTuiles)
            drawingPanel.setCurrentValue(i*15 + j);
        else
            drawingPanel.selectMode(null);
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

        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int j = 0;
        int i = 0;
        for(; i< nbTuiles; i++) {
            drawingPanel.drawTile(g, 5+j* CELL_SIZE*2, 5+(i%15)* CELL_SIZE*2, i, 1.7f);
            if ((i%15) == 14) {
                j++;
            }
        }
        // draw a red empty rectangle
        g.setColor(Color.RED);
        g.drawRect(5+j* CELL_SIZE*2, 5+(i%15)* CELL_SIZE*2, CELL_SIZE*2-1, CELL_SIZE*2-1);
    }


    public void mouseEntered(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / (CELL_SIZE*2);
        i = (evt.getX() - 5) / (CELL_SIZE*2);
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int val = i*15 + j;
        if (val < nbTuiles)
            this.setToolTipText(String.valueOf(val));
    }

    public void mouseMoved(MouseEvent e) {
        mouseEntered(e);
    }
}
