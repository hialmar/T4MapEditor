package net.torguet.t4maped;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static net.torguet.t4maped.T4DrawingPanel.CELL_SIZE;

public class T4TileAssemblerPanel  extends JPanel {
    private final T4DrawingPanel drawingPanel;
    private final T4PalettePanel palettePanel;

    private int currentTile;

    private final int[] currentSubTiles = new int[4];

    private int selectedSubTile = -1;

    private int subTileLine = -1;
    private int subTilePixel = -1;

    private static final int zoom = 8;

    private UndoCell firstUndo;
    private UndoCell lastUndo;
    private UndoCell currentUndo;

    private final int[] copiedSubTiles = new int[4];
    private String copiedComment;

    public T4TileAssemblerPanel(T4DrawingPanel drawingPanel, T4PalettePanel palettePanel) {
        this.drawingPanel = drawingPanel;
        this.palettePanel = palettePanel;
        this.palettePanel.setTileAssemblerPanel(this);
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(CELL_SIZE*8+10, CELL_SIZE*10+10));
        setBackground(Color.BLACK);
    }

    public void mousePressed(MouseEvent evt) {
        int i, j;
        if (evt.getX() < zoom*CELL_SIZE  + 5 ) {
            j = (evt.getY() - 5) / (zoom / 2 * CELL_SIZE);
            i = (evt.getX() - 5) / (zoom / 2 * CELL_SIZE);
            if (evt.getButton() == MouseEvent.BUTTON1)
                selectedSubTile = i + 2 * j;
            else if (evt.getButton() == MouseEvent.BUTTON3) {
                String s = (String)JOptionPane.showInputDialog(
                        this,
                        "Please type the comment for this tile",
                        "Tile Comment",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        drawingPanel.getCommentaireTuile()[currentTile]);
                if (s != null) {
                    if (!s.startsWith(";"))
                        s = "; " + s;
                    TileUndoCell newCell = new TileUndoCell(drawingPanel.getCommentaireTuile()[currentTile], s);

                    newCell.redoTileChanges(drawingPanel.getTuiles(), drawingPanel.getCommentaireTuile());

                    if (firstUndo == null) {
                        currentUndo = lastUndo = firstUndo = newCell;
                    } else {
                        lastUndo.setNextCell(newCell);
                        newCell.setPreviousCell(lastUndo);
                        currentUndo = lastUndo = newCell;
                    }
                    drawingPanel.setModified(true);
                }
            }

        } else {
            j = (evt.getY() - 5) / (zoom * CELL_SIZE / 6);
            i = (evt.getX() - 5 - zoom*CELL_SIZE) / (zoom * CELL_SIZE / 6);
            if (i>5) i = 5;
            if (j>5) j = 5;
            System.out.println("ligne "+j+" pixel "+i);
            subTileLine = j;
            subTilePixel = i;
            if (evt.getClickCount() == 1) {
                if (evt.getButton() == MouseEvent.BUTTON2) {
                    int val = drawingPanel.getSubTiles()[currentSubTiles[selectedSubTile]*6+subTileLine];
                    val ^= (int)Math.pow(2, 5-subTilePixel);
                    modifySubtile(val);
                } else if (evt.getButton() == MouseEvent.BUTTON3) {
                    int val = drawingPanel.getSubTiles()[currentSubTiles[selectedSubTile]*6+subTileLine];
                    val ^= (int)Math.pow(2, 7);
                    modifySubtile(val);
                }
            }
        }
        repaint();
    }

    private void modifySubtile(int val) {
        SubtileUndoCell newCell = new SubtileUndoCell(
                drawingPanel.getSubTiles()[currentSubTiles[selectedSubTile]*6+subTileLine], val,
                currentSubTiles[selectedSubTile], subTileLine);

        newCell.redoSubtileChanges(drawingPanel.getSubTiles());

        if (firstUndo == null) {
            currentUndo = lastUndo = firstUndo = newCell;
        } else {
            lastUndo.setNextCell(newCell);
            newCell.setPreviousCell(lastUndo);
            currentUndo = lastUndo = newCell;
        }
        repaintAll();
        drawingPanel.setModified(true);
    }


    public void mouseReleased() {
    }

    public void clear() {
        firstUndo = lastUndo = currentUndo = null;
        selectedSubTile = -1;
        currentTile = 0;
        for (int i = 0; i < 4; i++)
            currentSubTiles[i] = 0;
        subTileLine = -1;
        subTilePixel = -1;
    }

    public void refresh() {
        clear();
        computeSubTiles();
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

            drawingPanel.drawQuartTile(g, zoom*CELL_SIZE + 15,
                    5, currentSubTiles[selectedSubTile], zoom*2);

            g.setColor(Color.RED);
            if (subTileLine != -1 && subTilePixel != -1) {
                g.drawRect(zoom*CELL_SIZE  + 15 + subTilePixel*zoom*4,
                        5 + subTileLine * zoom * 4, zoom * 4, zoom * 4);
            }
        }
    }


    public void mouseEntered(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / (zoom/2*CELL_SIZE);
        i = (evt.getX() - 5) / (zoom/2*CELL_SIZE);
        int val = i+2*j;
        if (val >= 0 && val <= 3)
            this.setToolTipText(String.format("Tile %d($%02x) SubTile %d($%02x)",
                    currentTile,currentTile, currentSubTiles[val], currentSubTiles[val]));
        else
            this.setToolTipText(String.format("Tile %d($%02x) No SubTile",
                    currentTile,currentTile));
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
        if (selectedSubTile!=-1) {
            currentSubTiles[selectedSubTile]--;
            if (currentSubTiles[selectedSubTile] < 0)
                currentSubTiles[selectedSubTile] = drawingPanel.getNbSubtiles() / 6 - 1;
            updateSubTile();
        }
    }

    public void nextSubTile() {
        if (selectedSubTile!=-1) {
            currentSubTiles[selectedSubTile]++;
            if (currentSubTiles[selectedSubTile] > drawingPanel.getNbSubtiles() / 6 - 1)
                currentSubTiles[selectedSubTile] = 0;
            updateSubTile();
        }
    }


    private void updateSubTile() {
        TileUndoCell newCell = new TileUndoCell(drawingPanel.getTuiles()[currentTile*4+selectedSubTile],
                currentSubTiles[selectedSubTile], currentTile, selectedSubTile);
        redo(newCell);
    }

    public void undo() {
        if (currentUndo != null) {
            // undo
            if (currentUndo instanceof TileUndoCell tileUndoCell) {
                tileUndoCell.undoTileChanges(drawingPanel.getTuiles(), drawingPanel.getCommentaireTuile());
            } else if (currentUndo instanceof SubtileUndoCell subtileUndoCell) {
                subtileUndoCell.undoSubtileChanges(drawingPanel.getSubTiles());
            }
            selectedSubTile = -1;
            computeSubTiles();
            repaintAll();
            // move
            if (currentUndo.getPreviousCell() != null)
                currentUndo = currentUndo.getPreviousCell();
        }
    }

    public void redo() {
        if (currentUndo != null) {
            // redo
            if (currentUndo instanceof TileUndoCell tileUndoCell) {
                tileUndoCell.redoTileChanges(drawingPanel.getTuiles(), drawingPanel.getCommentaireTuile());
            } else if (currentUndo instanceof SubtileUndoCell subtileUndoCell) {
                subtileUndoCell.redoSubtileChanges(drawingPanel.getSubTiles());
            }
            repaintAll();
            drawingPanel.setModified(true);
            selectedSubTile = -1;
            computeSubTiles();
            // move
            if (currentUndo.getNextCell() != null)
                currentUndo = currentUndo.getNextCell();
        }
    }

    public void copy() {
        copiedComment = drawingPanel.getCommentaireTuile()[currentTile];
        System.arraycopy(currentSubTiles, 0, copiedSubTiles, 0,
                        currentSubTiles.length);
    }

    public void paste() {
        TileUndoCell newCell = new TileUndoCell(drawingPanel.getCommentaireTuile()[currentTile],
                copiedComment, currentSubTiles, copiedSubTiles, currentTile);
        redo(newCell);
        selectedSubTile = -1;
        computeSubTiles();
    }

    private void redo(TileUndoCell newCell) {
        newCell.redoTileChanges(drawingPanel.getTuiles(), drawingPanel.getCommentaireTuile());
        if (firstUndo == null) {
            currentUndo = lastUndo = firstUndo = newCell;
        } else {
            lastUndo.setNextCell(newCell);
            newCell.setPreviousCell(lastUndo);
            currentUndo = lastUndo = newCell;
        }
        repaintAll();
        drawingPanel.setModified(true);
    }

    public void newTile() {
        drawingPanel.addNewTile();
        computeSubTiles();
        repaintAll();
    }

    private void repaintAll() {
        repaint();
        drawingPanel.repaint();
        palettePanel.repaint();
    }

    public void loadTiles(String fileName) throws IOException {

    }

    public void saveTiles(String fileName) throws IOException {

    }

    private void computeSubTiles() {
        if (drawingPanel.getNbTuiles()>4) {
            for (int i = 0; i < 4; i++)
                currentSubTiles[i] = drawingPanel.getTuiles()[currentTile * 4 + i];
        }
    }

    public void setCurrentTile(int val) {
        currentTile = val;
        computeSubTiles();
        repaint();
    }
}
