/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.torguet.t4maped;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author torguet
 */
public class T4DrawingPanel extends JPanel {

    public static final int HEIGHT = 64;
    public static final int WIDTH = 64;
    public static final int CELL_SIZE = 24;
    private static final int MAX_FONT = 1024;
    public static final int pixelSize = 2;
    private int zoom = 1;
    private final int[][] laby = new int[HEIGHT][WIDTH];
    private int largeurLaby = 0;
    private int hauteurLaby = 0;
    private int couleurPair = 0;
    private int couleurImpair = 0;
    private final int[] quartTuiles = new int[MAX_FONT*4];
    private int nbQuartTuiles = 0;
    private final int[] tuiles = new int[MAX_FONT];
    private final String[] commentaireTuile = new String[MAX_FONT];
    private int nbTuiles = 0;

    private int currentValue = 1;

    private int readingIndex = 0;
    private boolean readingColors = false;
    private boolean readingQuartTuiles = false;
    private boolean readingTiles = false;
    private boolean readingMap = false;

    private UndoCell firstUndo;
    private UndoCell lastUndo;
    private UndoCell currentUndo;

    private boolean selectMode;
    private int selectStartI = -1;
    private int selectStartJ = -1;
    private int selectEndI = -1;
    private int selectEndJ = -1;

    private final int[][] copiedValues = new int[HEIGHT][WIDTH];
    private int copiedStartI = -1;
    private int copiedStartJ = -1;
    private int copiedEndI = -1;
    private int copiedEndJ = -1;

    private boolean modified = false;

    public T4DrawingPanel() {
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(WIDTH*CELL_SIZE+CELL_SIZE,HEIGHT*CELL_SIZE+CELL_SIZE));
        clear(false);
        setBackground(Color.BLACK);
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public int getNbTuiles() {
        return nbTuiles;
    }


    public int[] getTuiles() {
        return tuiles;
    }

    public String[] getCommentaireTuile() {
        return commentaireTuile;
    }

    public void clear(boolean onlyTiles) {
        if (!onlyTiles) {
            for (int[] laby1 : laby) Arrays.fill(laby1, 0);
            for (int[] laby1 : copiedValues) Arrays.fill(laby1, 0);
            largeurLaby = 0;
            hauteurLaby = 0;
            selectStartI = -1;
            selectStartJ = -1;
            selectEndI = -1;
            selectEndJ = -1;
            copiedStartI = -1;
            copiedStartJ = -1;
            copiedEndI = -1;
            copiedEndJ = -1;
            modified = false;
            firstUndo = lastUndo = currentUndo = null;
        }
        Arrays.fill(tuiles, 0);
        Arrays.fill(quartTuiles, 0);
        nbQuartTuiles = 0;
        nbTuiles = 0;
        readingIndex = 0;
        readingColors = false;
        readingQuartTuiles = false;
        readingTiles = false;
        readingMap = false;
        selectMode = true;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i = 0; i < laby.length; i++) {
            for (int j = 0; j < laby[i].length; j++) {
                if (j<largeurLaby && i < hauteurLaby) {
                    drawTile(g, j * CELL_SIZE * zoom + 4, i * CELL_SIZE * zoom +  4, laby[i][j], zoom);
                }
                if (selectMode) {
                    if (i>=selectStartI && i <= selectEndI && j>=selectStartJ && j<= selectEndJ) {
                        g.setColor(Color.RED);
                        g.drawRect(j * CELL_SIZE*zoom + 4, i * CELL_SIZE*zoom + 4, CELL_SIZE*zoom, CELL_SIZE*zoom);
                    }
                }
            }
        }
    }

    public void paintHorizontalCoordinates(Graphics g) {
        Rectangle visibleRectangle = this.getVisibleRect();
        //Graphics2D g2 = (Graphics2D) g;
        //g2.scale(zoom,zoom);
        g.setColor(Color.BLUE);
        int visibleJ = 0;
        int decal = -1;
        for (int j = 0; j < laby[0].length; j++) {
            if (j * CELL_SIZE * zoom + 4 >= visibleRectangle.getX()) {
                if (decal == -1)
                    decal = (int) (j * CELL_SIZE * zoom + 4 - visibleRectangle.getX());
                g.drawString("" + j, (visibleJ * CELL_SIZE * zoom + CELL_SIZE+10)+decal,
                        15);
                visibleJ++;
            }
        }
        //g2.scale(1.0/zoom,1.0/zoom);
    }

    public void paintVerticalCoordinates(Graphics g) {
        Rectangle visibleRectangle = this.getVisibleRect();
        //Graphics2D g2 = (Graphics2D) g;
        //g2.scale(zoom,zoom);
        g.setColor(Color.BLUE);
        int visibleI = 0;
        int decal = -1;
        for(int i = 0; i < laby.length; i++) {
            if (i * CELL_SIZE * zoom + 4 >= visibleRectangle.getY()) {
                if (decal == -1)
                    decal = (int) ((i * CELL_SIZE * zoom + 4) - visibleRectangle.getY());
                g.drawString(""+ i, 5,
                        (visibleI*CELL_SIZE*zoom+ CELL_SIZE+25)+decal);
                visibleI++;
            }
        }
        //g2.scale(1.0/zoom,1.0/zoom);
    }

    public void drawTile(Graphics g, int x, int y, int tile, float zoom) {
        int tileIndex = tile*4;
        int somme = tuiles[tileIndex];
        drawQuartTile(g, x,y, tuiles[tileIndex++], zoom);
        somme += tuiles[tileIndex];
        drawQuartTile(g, x+6*zoom*pixelSize,y, tuiles[tileIndex++], zoom);
        somme += tuiles[tileIndex];
        drawQuartTile(g, x,y+6*zoom*pixelSize, tuiles[tileIndex++], zoom);
        somme += tuiles[tileIndex];
        drawQuartTile(g, x+6*zoom*pixelSize,y+6*zoom*pixelSize, tuiles[tileIndex], zoom);

        if (somme == 0 && tile != 0) {
            g.setColor(Color.BLUE);
            Graphics2D g2 = (Graphics2D) g;
            g2.scale(zoom,zoom);
            g.drawString(String.format("%02x",tile), (int)(x/zoom)+5, (int)(y/zoom)+15);
            g2.scale(1.0/zoom,1.0/zoom);
        }

    }

    public void drawQuartTile(Graphics g, float x, float y, int tileIndex, float zoom) {
        int quartTuileIndex = tileIndex*6;
        for(int i=0; i<6; i++)
            drawQuartTileLine(g, x, y+i*zoom*pixelSize, quartTuiles[quartTuileIndex+i], i%2 == 0, zoom);
    }

    /*

NUMBER	STANDARD COLOR	INVERTED COLOR
0	    BLACK	        WHITE
1	    RED	            CYAN
2	    GREEN	        MAGENTA
3	    YELLOW	        BLUE
4	    BLUE	        YELLOW
5	    MAGENTA	        GREEN
6	    CYAN	        RED
7	    WHITE	        BLACK

*/
    Color[] colors = {Color.BLACK, Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.WHITE};

    Color[] invertedColors = {Color.WHITE, Color.CYAN, Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED, Color.BLACK};

    private void switchColor(Graphics g, boolean bit, boolean pair, boolean inverted) {
        if (inverted) {
            if (pair) {
                if (bit)
                    g.setColor(invertedColors[couleurPair]);
                else
                    g.setColor(Color.WHITE);
            } else {
                if (bit)
                    g.setColor(invertedColors[couleurImpair]);
                else
                    g.setColor(Color.WHITE);
            }
        } else {
            if (pair) {
                g.setColor(colors[couleurPair]);
            } else {
                g.setColor(colors[couleurImpair]);
            }
        }
    }



    private void drawQuartTileLine(Graphics g, float x, float y, int val, boolean pair, float zoom) {
        // Ensure we start in paint mode.
        g.setPaintMode();

        // découpage bits
        boolean inverse = (val & 0x80) > 0;
        // bit 6
        boolean bit6 = (val & 0x20) > 0;
        // bit 5
        boolean bit5 = (val & 0x10) > 0;
        // bit 4
        boolean bit4 = (val & 0x8) > 0;
        // bit 3
        boolean bit3 = (val & 0x4) > 0;
        // bit 2
        boolean bit2 = (val & 0x2) > 0;
        // bit 1
        boolean bit1 = (val & 0x1) > 0;


        if (bit6) {
            switchColor(g, bit6, pair, inverse);
            g.fillRect((int)x, (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        } else if (inverse) {
            switchColor(g, bit6, pair, inverse);
            g.fillRect((int)x, (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        }
        if (bit5) {
            switchColor(g, bit5, pair, inverse);
            g.fillRect((int)(x+pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        } else if (inverse) {
            switchColor(g, bit5, pair, inverse);
            g.fillRect((int)(x+pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        }
        if (bit4) {
            switchColor(g, bit4, pair, inverse);
            g.fillRect((int)(x+2*pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        } else if (inverse) {
            switchColor(g, bit4, pair, inverse);
            g.fillRect((int)(x+2*pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        }
        if (bit3) {
            switchColor(g, bit3, pair, inverse);
            g.fillRect((int)(x+3*pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        } else if (inverse) {
            switchColor(g, bit3, pair, inverse);
            g.fillRect((int)(x+3*pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        }
        if (bit2) {
            switchColor(g, bit2, pair, inverse);
            g.fillRect((int)(x+4*pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        } else if (inverse) {
            switchColor(g, bit2, pair, inverse);
            g.fillRect((int)(x+4*pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        }
        if (bit1){
            switchColor(g, bit1, pair, inverse);
            g.fillRect((int)(x+5*pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        } else if (inverse) {
            switchColor(g, bit1, pair, inverse);
            g.fillRect((int)(x+5*pixelSize * zoom), (int)y, (int)(pixelSize * zoom), (int)(pixelSize * zoom));
        }
    }



    void mousePressed(MouseEvent evt) {
        int i, j;
        i = (evt.getY() - 4) / (CELL_SIZE*zoom);
        j = (evt.getX() - 4) / (CELL_SIZE*zoom);

        if(i < HEIGHT && j < WIDTH) {
            if (selectMode) {
                selectStartI = selectEndI = i;
                selectStartJ = selectEndJ = j;
            } else {
                UndoCell newCell = new UndoCell(laby[i][j], currentValue, i, j);
                if (firstUndo == null) {
                    currentUndo = lastUndo = firstUndo = newCell;
                } else {
                    lastUndo.setNextCell(newCell);
                    newCell.setPreviousCell(lastUndo);
                    currentUndo = lastUndo = newCell;
                }

                laby[i][j] = currentValue;

                modified = true;

                if (i+1 > hauteurLaby) {
                    newCell.expandHeight(i+1, hauteurLaby);
                    hauteurLaby = i+1;
                }
                if (j+1 > largeurLaby) {
                    newCell.expandWidth(j+1, largeurLaby);
                    largeurLaby = j+1;
                }
            }
        }
        repaint();
    }

    public void selectMode() {
        selectMode = true;
    }

    public void mouseDragged(MouseEvent evt) {
        if (selectMode) {
            int i, j;
            i = (evt.getY() - CELL_SIZE*zoom - 4) / (CELL_SIZE*zoom);
            j = (evt.getX() - CELL_SIZE*zoom - 4) / (CELL_SIZE*zoom);

            if(i < HEIGHT && j < WIDTH) {
                selectEndI = i;
                selectEndJ = j;

                repaint();
            }
        } else {
            mousePressed(evt);
        }
    }


    void mouseReleased() {
    }

    public void mouseEntered(MouseEvent evt) {
        int i, j;
        i = (evt.getY() -  4) / (CELL_SIZE*zoom);
        j = (evt.getX() -  4) / (CELL_SIZE*zoom);
        if(i>=0 && i < HEIGHT && j >=0 && j < WIDTH) {
            this.setToolTipText(String.format("%d, %d:%d($%02x) %s",
                    i, j, laby[i][j], laby[i][j], commentaireTuile[laby[i][j]]));
        }
    }

    public void mouseMoved(MouseEvent e) {
        mouseEntered(e);
    }


    public void copy() {
        for(int i = 0; i<selectEndI-selectStartI+1;i++) {
            if (selectEndJ - selectStartJ + 1 >= 0)
                System.arraycopy(laby[selectStartI + i], selectStartJ, copiedValues[i], 0,
                        selectEndJ - selectStartJ + 1);
        }
        copiedStartI = selectStartI;
        copiedStartJ = selectStartJ;
        copiedEndI = selectEndI;
        copiedEndJ = selectEndJ;
    }

    public void paste() {
        if (copiedEndJ-copiedStartJ<selectEndJ-selectStartJ ||
            copiedEndI-copiedStartI<selectEndI-selectStartI)
        {
            JOptionPane.showMessageDialog(this,
                    "The destination region should be smaller or equal to the copied region of size : ("+
                            (copiedEndJ-copiedStartJ+1)+","+(copiedEndI-copiedStartI+1)+")",
                    "Region Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            RegionUndoCell newCell = new RegionUndoCell(selectStartI, selectStartJ, selectEndI, selectEndJ, laby, copiedValues);
            if (firstUndo == null) {
                currentUndo = lastUndo = firstUndo = newCell;
            } else {
                lastUndo.setNextCell(newCell);
                newCell.setPreviousCell(lastUndo);
                currentUndo = lastUndo = newCell;
            }
            newCell.redoLabyChanges(laby);
            if (selectEndJ+1 > this.largeurLaby)
                largeurLaby = selectEndJ+1;
            if (selectEndI+1 > this.hauteurLaby)
                hauteurLaby = selectEndI+1;
            repaint();
        }
    }


    void undo() {
        if (currentUndo != null) {
            // undo
            if (currentUndo instanceof RegionUndoCell regionUndoCell) {
                regionUndoCell.undoLabyChanges(laby);
            } else {
                laby[currentUndo.getI()][currentUndo.getJ()] = currentUndo.getPreviousValue();
                if (currentUndo.isExpandedWidth()) {
                    largeurLaby = currentUndo.getPreviousWidth();
                }
                if (currentUndo.isExpandedHeight()) {
                    hauteurLaby = currentUndo.getPreviousHeight();
                }
            }
            repaint();
            // move
            if (currentUndo.getPreviousCell() != null)
                currentUndo = currentUndo.getPreviousCell();
        }
    }

    void redo() {
        if (currentUndo != null) {
            // undo
            if (currentUndo instanceof RegionUndoCell regionUndoCell) {
                regionUndoCell.redoLabyChanges(laby);
            } else {
                laby[currentUndo.getI()][currentUndo.getJ()] = currentUndo.getValue();
                if (currentUndo.isExpandedWidth()) {
                    largeurLaby = currentUndo.getWidth();
                }
                if (currentUndo.isExpandedHeight()) {
                    hauteurLaby = currentUndo.getHeight();
                }
            }
            repaint();
            // move
            if (currentUndo.getNextCell() != null)
                currentUndo = currentUndo.getNextCell();
        }
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
        this.selectMode = false;
    }
    
    public void saveLaby(String fileName, boolean onlyTiles) throws IOException {
        PrintWriter file = new PrintWriter(new FileWriter(fileName));

        saveHiresAndAttribs(file);

        if (!onlyTiles)
            saveLabyData(file);

        saveSubtiles(file);

        saveTiles(file);

        saveSubtilesAddresses(file);

        file.close();

        modified = false;
    }

    private void saveHiresAndAttribs(PrintWriter file) {
        // hires_et_attributs
        file.println("""
                hires_et_atributs
                \t\tjsr $EC33""");
        for(int adrDebutBloc = 0xaa01; adrDebutBloc <= 0xb541; adrDebutBloc += 0x1e0) {
            file.println(String.format("\t\tlda #$%02x", couleurPair));
            for (int adresse = adrDebutBloc; adresse <= adrDebutBloc + 0x190; adresse += 0x50)
                file.println(String.format("\t\tsta $%x", adresse));
            file.println(String.format("\t\tlda #$%02x", couleurImpair));
            for (int adresse = adrDebutBloc + 0x28; adresse <= adrDebutBloc + 0x1b8; adresse += 0x50)
                file.println(String.format("\t\tsta $%x", adresse));
        }
        file.println("\t\trts");
    }

    private static void saveSubtilesAddresses(PrintWriter file) {
        file.println("; -----------------------------------------------------------------------------");
        file.println(";    Table adresses car modifies dans 2nd jeu de car mode Hires (1/4 de tuile)");
        file.println("; -----------------------------------------------------------------------------");

        file.println("sous_tuile");

        for(int i=0; i<0x9ff5-0x9d00; i++) {
            if (i%0x3c == 0)
                file.print("\t.byt ");
            if (i%6 == 0) {
                int adresse = 0x9d00+i;
                String adresseHiStr = String.format("%x",adresse/256);
                String adresseLoStr = String.format("%02x",adresse%256);
                file.print("$"+adresseHiStr+",$"+adresseLoStr);
                if(i%0x3c == 0x36)
                    file.println();
                else if (adresse!=0x9ff4)
                    file.print(",");
            }
        }

        file.println();
    }

    private void saveTiles(PrintWriter file) {
        file.println("; --------------------------------------------------------------------");
        file.println(";    Table redefinition  des tuiles (N)d'ordre des 4 car redefinis");
        file.println("; --------------------------------------------------------------------");

        for(int i=0; i<this.nbTuiles; i++) {
            if(i%4 == 0) {
                String tileNumber = String.format("_t%02x ",i/4);
                file.println(tileNumber);
                file.print("\t\t.byt ");
            }
            String quartTile = String.format("$%02x", tuiles[i]);
            file.print(quartTile);
            if (i%4 != 3)
                file.print(",");
            else
                file.println(" "+commentaireTuile[i/4]);
        }

        file.println("; -----------------------------------------------");
        file.println(";       Table des pointeurs adresse tuiles  ");
        file.println("; -----------------------------------------------");
        file.println("ptr_t ;(pointeurs t pour tuiles)");

        for (int nbTuile = 0; nbTuile <this.nbTuiles/4; nbTuile++) {
            if (nbTuile % 6 == 0)
                file.print("\t.byt ");
            String tileNumber = String.format("%02x", nbTuile);
            file.print("<_t" + tileNumber + ",>_t" + tileNumber);
            if (nbTuile % 6 < 5 && nbTuile < this.nbTuiles/4-1)
                file.print(",");
            if (nbTuile % 6 == 5)
                file.println();
        }

        file.println();
    }

    private void saveSubtiles(PrintWriter file) {
        file.println();
        file.println("; -----------------------------------------------");
        file.println(";       Table redéfinition  2nd jeu de car");
        file.println("; -----------------------------------------------");

        file.println("dta_car_redef_p1");
        for(int i=0; i<this.nbQuartTuiles; i++) {
            if (i%6 == 0) {
                String carNumber = String.format("%02X",i/6);
                int adresse = 0x9d00 + i;
                String adresseStr = String.format("%x",adresse);
                file.println(";"+carNumber+" en $"+adresseStr);
            }
            String val = String.format("%02x",quartTuiles[i]);
            StringBuilder binaire = new StringBuilder();
            int quartTuile = quartTuiles[i];
            int diviseur = 128;
            while (diviseur > 0) {
                if (quartTuile/diviseur == 1) {
                    binaire.append('1');
                    quartTuile -= diviseur;
                } else {
                    binaire.append('0');
                }
                if (diviseur>1)
                    binaire.append(',');
                diviseur/=2;
            }
            file.println("\t.byt $"+val+"	;"+binaire);
            if (i%6 == 5) {
                file.println();
                if(i/6==0x29) {
                    file.println("dta_car_redef_p2");
                }
                if(i/6==0x53) {
                    file.println("dta_car_redef_p3");
                }
            }
        }
    }

    private void saveLabyData(PrintWriter file) {
        // first find the maxI and maxJ values
        int maxI = 0;
        int maxJ = 0;
        for (int i = 0; i < laby.length; i++) {
            int tempoMaxJ=0;
            for (int j = 0; j < laby[i].length; j++) {
                if(laby[i][j]>0) {
                    if (i>maxI) maxI = i;
                    if (j>tempoMaxJ) tempoMaxJ = j;
                }
            }
            if(tempoMaxJ > maxJ) maxJ = tempoMaxJ;
        }

        // save the laby

        /*

        ;*******************************************
;*******    DATA PLAN VILLE_1   ************
;*******************************************
_L00
	.byt $13,$02,$01,$02,$01,$02,$01,$02,$01,$13,$02,$01,$02,$01,$02,$01,$02,$01,$02,$01,$02,$01,$02,$01,$02,$01,$02,$01,$03,$04

         */
        file.println("\t;*******************************************");
        file.println("\t;*******    DATA PLAN/VILLE XX  ************");
        file.println("\t;*******************************************");
        for (int i = 0; i < maxI+1; i++) {
            String lineNumber = String.format("%02d",i);
            file.println("_L"+lineNumber);
            file.print("\t.byt ");
            for (int j = 0; j < maxJ+1; j++) {
                file.print(String.format("$%02x",laby[i][j]));
                if(j+1<maxJ+1)
                    file.print(",");
            }
            file.println();
        }
/*
        ptr_Lignes

                .byt <_L00,>_L00,<_L01,>_L01,<_L02,>_L02,<_L03,>_L03,<_L04,>_L04,<_L05,>_L05,<_L06,>_L06,<_L07,>_L07,<_L08,>_L08,<_L09,>_L09
                .byt <_L10,>_L10,<_L11,>_L11,<_L12,>_L12,<_L13,>_L13,<_L14,>_L14,<_L15,>_L15,<_L16,>_L16,<_L17,>_L17,<_L18,>_L18,<_L19,>_L19
*/
        file.println();
        file.println("ptr_Lignes");
        file.println();
        for (int i = 0; i < maxI+1; i++) {
            if (i % 10 == 0)
                file.print("\t.byt ");
            String lineNumber = String.format("%02d", i);
            file.print("<_L" + lineNumber + ",>_L" + lineNumber);
            if (i % 10 < 9 && i < maxI)
                file.print(",");
            if (i % 10 == 9)
                file.println();
        }
    }


    public void loadLaby(String fileName, boolean onlyTiles) throws IOException {
        clear(onlyTiles);
        if (!onlyTiles) {
            modified = false;
        }
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        // read the colors
        String line;
        while((line = file.readLine()) != null) {
            if (line.startsWith("hires_et_atributs")) {
                readingColors = true;
            } else if(line.startsWith("_L00")) {
                if (!onlyTiles) {
                    readingMap = true;
                    System.out.println("Début Map");
                }
            } else if(line.startsWith("dta_car_redef_p1")) {
                endReadingTuiles();
                readingQuartTuiles = true;
                System.out.println("Début Quarts de Tuiles");
                readingIndex = 0;
            }
            else if (line.startsWith("_t00")) {
                endReadingQuartTuiles();
                readingTiles = true;
                readingIndex = 0;
                System.out.println("Début Tuiles");
            }
            else if (readingMap) {
                if (line.contains(".byt")) {
                    String [] tab = line.strip().split("[$ ,]");
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(tab));
                    // System.out.println(list);
                    list.removeAll(Arrays.asList("", null));
                    // System.out.println(list);
                    largeurLaby=0;
                    for(String oct : list) {
                        if (oct.equalsIgnoreCase(".byt"))
                            continue;
                        try {
                            laby[hauteurLaby][largeurLaby] = Integer.parseInt(oct,16);
                            largeurLaby++;
                        } catch (NumberFormatException e) {
                            Logger.getLogger(T4DrawingPanel.class.getName()).log(Level.FINEST, null, e);
                        }
                    }
                    hauteurLaby++;
                } else if (line.contains("ptr_Lignes")) {
                    endReadingMap();
                }
            }
            else if (readingColors) {
                readingColors(line);
            }  else if (readingQuartTuiles) {
                readingQuartTuile(line);
            }
            else if (readingTiles) {
                if (line.contains(".byt")) {
                    readingTuile(line);
                } else if (line.startsWith("ptr_t")) {
                    nbTuiles = readingIndex;
                    System.out.println("Tuiles total : "+nbTuiles);
                    System.out.println("Tuiles div 4 : "+(nbTuiles/4));

                    if (nbQuartTuiles > 0) {
                        // on a tout lu
                        file.close();
                        repaint();
                        return;
                    }
                }
            }
        }
        endReadingQuartTuiles();
        endReadingTuiles();
        file.close();
        repaint();
    }

    private void readingTuile(String line) {
        String[] tab = line.strip().split("[$ ,\t]");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(tab));
        // System.out.println(list);
        list.removeAll(Arrays.asList("", null));
        // System.out.println(list);
        StringBuilder commentaire = new StringBuilder();
        boolean debutCom = false;
        for (String oct : list) {
            if (oct.startsWith(";")) {// comment
                debutCom = true;
                commentaire.append(oct).append(" ");
            } else if (debutCom) {
                commentaire.append(oct).append(" ");
            } else {
                try {
                    tuiles[readingIndex] = Integer.parseInt(oct, 16);
                    readingIndex++;
                } catch (NumberFormatException e) {
                    Logger.getLogger(T4DrawingPanel.class.getName()).log(Level.FINEST, null, e);
                }
            }
        }
        commentaireTuile[nbTuiles++] = commentaire.toString();
    }

    private void readingQuartTuile(String line) {
        if (line.contains(".byt")) {
            String[] tab = line.strip().split("[$ ,\t]");
            ArrayList<String> list = new ArrayList<>(Arrays.asList(tab));
            // System.out.println(list);
            list.removeAll(Arrays.asList("", null));
            // System.out.println(list);
            for (String oct : list) {
                if (oct.startsWith(";")) { // comment
                    break;
                } else {
                    try {
                        quartTuiles[readingIndex] = Integer.parseInt(oct, 16);
                        readingIndex++;
                        break; // 1 seul par ligne
                    } catch (NumberFormatException e) {
                        Logger.getLogger(T4DrawingPanel.class.getName()).log(Level.FINEST, null, e);
                    }
                }
            }
        }
    }

    private void endReadingMap() {
        System.out.println("Fin Map");
        System.out.println("Nb Colonnes "+(largeurLaby-1));
        System.out.println("Nb Lignes "+(hauteurLaby-1));
        readingMap = false;
    }

    private void readingColors(String line) {
        if(line.contains("lda")) {
            String [] tab = line.strip().split(" ");
            int couleur;
            if (tab[1].startsWith("#$")) {
                couleur = Integer.parseInt(tab[1].substring(2),16);
                System.out.println("Couleur Trouvée : "+couleur);
            } else if (tab[1].startsWith("#")) {
                couleur = Integer.parseInt(tab[1].substring(1));
                System.out.println("Couleur Trouvée : "+couleur);
            } else {
                System.err.println("Wrong color at line : "+line);
                couleur = 3;
            }
            if (readingIndex==0) {
                couleurPair = couleur;
                System.out.println("Couleur Paire : "+couleur);
                readingIndex++;
            } else {
                couleurImpair = couleur;
                System.out.println("Couleur Impaire : "+couleur);
                readingColors = false;
            }
        }
    }

    private void endReadingTuiles() {
        if (readingTiles) {
            nbTuiles = readingIndex;
            System.out.println("Tuiles total : "+nbTuiles);
            System.out.println("Tuiles div 4 : "+(nbTuiles/4));
            readingTiles = false;
        }
    }

    private void endReadingQuartTuiles() {
        if (readingQuartTuiles) {
            nbQuartTuiles = readingIndex;
            System.out.println("Quart de tuiles total : " + nbQuartTuiles);
            System.out.println("Quart de tuiles div 6 : " + (nbQuartTuiles / 6));
            readingQuartTuiles = false;
        }
    }

    public void setZoom(int i) {
        zoom = i;
        setPreferredSize(new Dimension(WIDTH*CELL_SIZE*zoom+CELL_SIZE*zoom,HEIGHT*CELL_SIZE*zoom+CELL_SIZE*zoom));
        repaint();
    }

    public void emptyLaby() {
        selectStartI = 0;
        selectEndI = hauteurLaby;
        selectStartJ = 0;
        selectEndJ = largeurLaby;

        for (int[] laby1 : copiedValues) Arrays.fill(laby1, 0);

        RegionUndoCell newCell = new RegionUndoCell(selectStartI, selectStartJ, selectEndI, selectEndJ, laby, copiedValues);
        if (firstUndo == null) {
            currentUndo = lastUndo = firstUndo = newCell;
        } else {
            lastUndo.setNextCell(newCell);
            newCell.setPreviousCell(lastUndo);
            currentUndo = lastUndo = newCell;
        }
        newCell.redoLabyChanges(laby);
        if (selectEndJ + 1 > this.largeurLaby)
            largeurLaby = selectEndJ + 1;
        if (selectEndI + 1 > this.hauteurLaby)
            hauteurLaby = selectEndI + 1;
        repaint();
    }

    public int[] getSubTiles() {
        return quartTuiles;
    }

    public int getNbSubtiles() {
        return nbQuartTuiles;
    }

    public void addNewTile() {
        if (nbTuiles+4<MAX_FONT) {
            nbTuiles += 4;
            setModified(true);
            commentaireTuile[nbTuiles/4-1] = "";
        } else {
            JOptionPane.showMessageDialog(this,
                    "Too many tiles",
                    "Too many tiles error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public int newSubtile() {
        if(nbQuartTuiles+6<MAX_FONT*4) {
            int oldValue = nbQuartTuiles;
            nbQuartTuiles += 6;
            return oldValue;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Too many subtiles",
                    "Too many subtiles error",
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    public void chooseEvenColor() {
        int newColorIndex = chooseColor(couleurPair);
        if (newColorIndex >= 0) {
            couleurPair = newColorIndex;
        }
    }

    public void chooseOddColor() {
        int newColorIndex = chooseColor(couleurImpair);
        if (newColorIndex >= 0) {
            couleurImpair = newColorIndex;
        }
    }

    private int chooseColor(int oldColorIndex) {
        Color oldColor = colors[oldColorIndex];
        Color newColor = JColorChooser.showDialog(
                this,
                "Choose Even Color",
                oldColor);
        if (newColor != null) {
            for(int i=0; i<colors.length; i++) {
                if (colors[i].equals(newColor)) {
                    return i;
                }
            }
            JOptionPane.showMessageDialog(this,
                    "This color was not an Oric color",
                    "Color error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }


}
