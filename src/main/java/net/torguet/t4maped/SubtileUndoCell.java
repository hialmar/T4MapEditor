package net.torguet.t4maped;

public class SubtileUndoCell extends UndoCell {
    public SubtileUndoCell(int previousValue, int value, int i, int j) {
        super(previousValue, value, i, j);
    }

    public void undoSubtileChanges(int[] subTiles) {
        subTiles[getI()*6+getJ()] = getPreviousValue();
    }

    public void redoSubtileChanges(int[] subTiles) {
        subTiles[getI()*6+getJ()] = getValue();
    }
}
