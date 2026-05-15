/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.torguet.t4maped;

import javax.swing.*;
import java.awt.*;

import static net.torguet.t4maped.T4DrawingPanel.CELL_SIZE;

/**
 *
 * @author torguet
 */
public class T4HorizontalRulerPanel extends JPanel {
    private final T4DrawingPanel drawingPanel;

    public T4HorizontalRulerPanel(T4DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        this.setPreferredSize(new Dimension(WIDTH*CELL_SIZE+CELL_SIZE,CELL_SIZE));
        this.setMinimumSize(new Dimension(WIDTH*CELL_SIZE+CELL_SIZE,CELL_SIZE));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawingPanel.paintHorizontalCoordinates(g);
    }
}
