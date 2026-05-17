package net.torguet.t4maped;

public class RegionUndoCell extends UndoCell {
    private final int selectStartI;
    private final int selectStartJ;
    private final int selectEndI;
    private final int selectEndJ;

    private final int[][] previousValues = new int[T4DrawingPanel.HEIGHT][T4DrawingPanel.HEIGHT];
    private final int[][] currentValues = new int[T4DrawingPanel.HEIGHT][T4DrawingPanel.HEIGHT];


    public RegionUndoCell(int selectStartI, int selectStartJ, int selectEndI, int selectEndJ,
                          int[][] laby, int[][] copiedValues) {
        super(0, 0, -1, -1);
        this.selectStartI = selectStartI;
        this.selectStartJ = selectStartJ;
        this.selectEndI = selectEndI;
        this.selectEndJ = selectEndJ;

        for(int i = 0; i<selectEndI-selectStartI+1;i++) {
            for(int j = 0; j<selectEndJ-selectStartJ+1; j++) {
                previousValues[i][j] = laby[selectStartI+i][selectStartJ+j];
                currentValues[i][j] = copiedValues[i][j];
            }
        }
    }

    public RegionUndoCell(int selectStartI, int selectStartJ, int selectEndI, int selectEndJ, int[][] laby) {
        this(selectStartI, selectStartJ, selectEndI, selectEndJ, laby, 0);
    }

    public RegionUndoCell(int selectStartI, int selectStartJ, int selectEndI, int selectEndJ, int[][] laby, int value) {
        super(0, 0, -1, -1);
        this.selectStartI = selectStartI;
        this.selectStartJ = selectStartJ;
        this.selectEndI = selectEndI;
        this.selectEndJ = selectEndJ;

        for(int i = 0; i<selectEndI-selectStartI+1;i++) {
            for(int j = 0; j<selectEndJ-selectStartJ+1; j++) {
                previousValues[i][j] = laby[selectStartI+i][selectStartJ+j];
                currentValues[i][j] = value;
            }
        }
    }

    void undoLabyChanges(int[][] laby) {
        for(int i = 0; i<selectEndI-selectStartI+1;i++) {
            if (selectEndJ - selectStartJ + 1 >= 0)
                System.arraycopy(previousValues[i], 0, laby[selectStartI + i],
                        selectStartJ, selectEndJ - selectStartJ + 1);
        }
    }

    void redoLabyChanges(int[][] laby) {
        for(int i = 0; i<selectEndI-selectStartI+1;i++) {
            if (selectEndJ - selectStartJ + 1 >= 0)
                System.arraycopy(currentValues[i], 0, laby[selectStartI + i],
                        selectStartJ, selectEndJ - selectStartJ + 1);
        }
    }
}
