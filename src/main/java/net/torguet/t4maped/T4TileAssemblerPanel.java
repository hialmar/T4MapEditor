package net.torguet.t4maped;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static net.torguet.t4maped.T4DrawingPanel.CELL_SIZE;

public class T4TileAssemblerPanel  extends JPanel {
    private final T4DrawingPanel drawingPanel;
    private final T4PalettePanel palettePanel;
    private final T4SubtilePalettePanel subTilePalettePanel;
    private final T4TileAssemblerFrame tileAssemblerFrame;

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
    private boolean designMode;

    public T4TileAssemblerPanel(T4DrawingPanel drawingPanel, T4PalettePanel palettePanel, T4SubtilePalettePanel subtilePalettePanel, T4TileAssemblerFrame tileAssemblerFrame) {
        this.drawingPanel = drawingPanel;
        this.palettePanel = palettePanel;
        this.palettePanel.setTileAssemblerPanel(this);
        this.subTilePalettePanel = subtilePalettePanel;
        this.tileAssemblerFrame = tileAssemblerFrame;
        subtilePalettePanel.setTileAssemblerPanel(this);
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(CELL_SIZE*8+10, CELL_SIZE*10+10));
        setBackground(Color.BLACK);
        designMode = false;
    }

    public void mousePressed(MouseEvent evt) {
        if (drawingPanel.getNbTuiles() == 0 || drawingPanel.getNbSubtiles() == 0)
            return;
        int i, j;
        if (evt.getX() < zoom*CELL_SIZE  + 5 ) {
            j = (evt.getY() - 5) / (zoom / 2 * CELL_SIZE);
            i = (evt.getX() - 5) / (zoom / 2 * CELL_SIZE);
            selectedSubTile = i + 2 * j;
            if (selectedSubTile > 3) {
                selectedSubTile = -1;
            } else {
                if (designMode) {
                    j = (evt.getY() - 5) / (zoom * CELL_SIZE / 12);
                    i = (evt.getX() - 5) / (zoom * CELL_SIZE / 12);
                    if (i > 11) i = 11;
                    if (j > 11) j = 11;
                    System.out.println("ligne " + j + " pixel " + i);
                    if (i > 5) i -= 6;
                    if (j > 5) j -= 6;
                    System.out.println("In SubTile " + selectedSubTile + " ligne " + j + " pixel " + i);
                    editSubTile(evt, i, j);
                } else {
                    if (evt.getButton() == MouseEvent.BUTTON3) {
                        modifyTileComment();
                    }
                }
            }
        } else {
            j = (evt.getY() - 5) / (zoom * CELL_SIZE / 6);
            i = (evt.getX() - 5 - zoom*CELL_SIZE) / (zoom * CELL_SIZE / 6);
            if (selectedSubTile!= -1 && i<=5 && j<=5) {
                System.out.println("ligne " + j + " pixel " + i);
                editSubTile(evt, i, j);
            }
        }
        repaint();
    }

    private void editSubTile(MouseEvent evt, int i, int j) {
        subTileLine = j;
        subTilePixel = i;
        if (evt.getClickCount() == 1) {
            int onMaskC1 = MouseEvent.CTRL_DOWN_MASK | MouseEvent.BUTTON1_DOWN_MASK;
            int onMaskC3 = MouseEvent.CTRL_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
            if (evt.getButton() == MouseEvent.BUTTON1) {
                if ((evt.getModifiersEx() & (onMaskC1)) == onMaskC1) {
                    int val = drawingPanel.getSubTiles()[currentSubTiles[selectedSubTile] * 6 + subTileLine];
                    val |= 1 << 7;
                    modifySubtile(val);
                } else {
                    int val = drawingPanel.getSubTiles()[currentSubTiles[selectedSubTile] * 6 + subTileLine];
                    val |= 1 << (5 - subTilePixel);
                    modifySubtile(val);
                }
            } else if (evt.getButton() == MouseEvent.BUTTON3) {
                if ((evt.getModifiersEx() & (onMaskC3)) == onMaskC3) {
                    int val = drawingPanel.getSubTiles()[currentSubTiles[selectedSubTile] * 6 + subTileLine];
                    val &= ~(1 << 7);
                    modifySubtile(val);
                } else {
                    int val = drawingPanel.getSubTiles()[currentSubTiles[selectedSubTile] * 6 + subTileLine];
                    val &= ~(1 << (5 - subTilePixel));
                    modifySubtile(val);
                }
            }
        }
    }

    public void modifyTileComment() {
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
            TileUndoCell newCell = new TileUndoCell(drawingPanel.getCommentaireTuile()[currentTile], s, currentTile);

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
        designMode = false;
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
        if (selectedSubTile >= 0 && selectedSubTile <=3) {
            g.drawRect(5 + (selectedSubTile&0x1)*(zoom/2*CELL_SIZE),
                    5 + (selectedSubTile&0x2)/2*(zoom/2*CELL_SIZE),
                    zoom/2*CELL_SIZE - 1, zoom/2*CELL_SIZE - 1);

            drawingPanel.drawQuartTile(g, zoom*CELL_SIZE + 15,
                    5, currentSubTiles[selectedSubTile], zoom*2);

            g.setColor(Color.LIGHT_GRAY);
            for(int line = 0; line < 6; line++) {
                for(int pixel = 0; pixel < 6; pixel++) {
                    g.drawRect(zoom*CELL_SIZE  + 15 + pixel*zoom*4,
                            5 + line * zoom * 4, zoom * 4, zoom * 4);
                }
            }
            if (subTileLine != -1 && subTilePixel != -1) {
                g.setColor(Color.RED);
                g.drawRect(zoom*CELL_SIZE  + 15 + subTilePixel*zoom*4,
                        5 + subTileLine * zoom * 4, zoom * 4-1, zoom * 4-1);
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
        endDesignMode();
    }

    public void nextTile() {
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        currentTile++;
        if (currentTile>=nbTuiles)
            currentTile = 0;
        repaint();
        computeSubTiles();
        endDesignMode();
    }

    public void previousSubTile() {
        if (selectedSubTile!=-1) {
            currentSubTiles[selectedSubTile]--;
            if (currentSubTiles[selectedSubTile] < 0)
                currentSubTiles[selectedSubTile] = drawingPanel.getNbSubtiles() / 6 - 1;
            updateSubTile();
            endDesignMode();
        }
    }

    public void nextSubTile() {
        if (selectedSubTile!=-1) {
            currentSubTiles[selectedSubTile]++;
            if (currentSubTiles[selectedSubTile] > drawingPanel.getNbSubtiles() / 6 - 1)
                currentSubTiles[selectedSubTile] = 0;
            updateSubTile();
            endDesignMode();
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
            endDesignMode();
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
            endDesignMode();
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
        endDesignMode();
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
        int oldTile = currentTile;
        currentTile = drawingPanel.addNewTile();
        if (currentTile == -1) {
            currentTile = oldTile;
        } else {
            if (drawingPanel.getNbSubtiles()==0) {
                // create the first sub tile
                selectedSubTile = 0;
                newSubtile();
            }
            computeSubTiles();
            repaintAll();
            endDesignMode();
        }
    }

    private void repaintAll() {
        repaint();
        drawingPanel.repaint();
        palettePanel.repaint();
        subTilePalettePanel.repaint();
    }

    private void computeSubTiles() {
        if (drawingPanel.getNbTuiles()>4) {
            for (int i = 0; i < 4; i++)
                currentSubTiles[i] = drawingPanel.getTuiles()[currentTile * 4 + i];
        }
        palettePanel.selectTile(currentTile);
    }

    public void setCurrentTile(int val) {
        currentTile = val;
        palettePanel.selectTile(currentTile);
        computeSubTiles();
        endDesignMode();
        repaint();
    }

    public void newSubtile() {
        if (selectedSubTile == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please first select a subtile in the current tile",
                    "Subtile creation error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int newSubtile = drawingPanel.newSubtile();
            if (newSubtile > 0) {
                currentSubTiles[selectedSubTile] = newSubtile / 6;
                updateSubTile();
            }
            endDesignMode();
        }
    }

    public void setCurrentSubTile(int val) {
        if (selectedSubTile!=-1) {
            currentSubTiles[selectedSubTile]=val;
            updateSubTile();
        }
    }

    public void designNewTile() {
        int oldTile = currentTile;
        currentTile = drawingPanel.addNewTile();
        if (currentTile>=0) {
            for (selectedSubTile = 0; selectedSubTile < currentSubTiles.length; selectedSubTile++) {
                int newSubtile = drawingPanel.newSubtile();
                if (newSubtile >= 0) {
                    currentSubTiles[selectedSubTile] = newSubtile / 6;
                    updateSubTile();
                }
            }
            startDesignMode();
            palettePanel.selectTile(currentTile);
        } else {
            currentTile = oldTile;
        }
        selectedSubTile = -1;
        computeSubTiles();
        repaintAll();
    }

    public void endDesignMode() {
        designMode = false;
        tileAssemblerFrame.setTitle("T4 Tile Editor: Edit Mode");
    }

    public void startDesignMode() {
        designMode = true;
        tileAssemblerFrame.setTitle("T4 Tile Editor: Design Mode");
    }
}
