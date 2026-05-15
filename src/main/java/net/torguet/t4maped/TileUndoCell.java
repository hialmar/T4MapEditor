package net.torguet.t4maped;

public class TileUndoCell extends UndoCell {
    enum TypeModif {
        SubTile, Comment, Everything
    }

    TypeModif typeModif;

    String previousComment;
    String comment;

    private final int[] previousSubtiles = new int[4];
    private final int[] currentSubtiles = new int[4];

    public TileUndoCell(int previousValue, int value, int currentTile, int subTileNumber) {
        super(previousValue, value, currentTile, subTileNumber);
        typeModif = TypeModif.SubTile;
    }

    public TileUndoCell(String oldComment, String newComment, int currentTile) {
        super(0, 0, currentTile, 0);
        typeModif = TypeModif.Comment;
        previousComment = oldComment;
        comment = newComment;
    }

    public TileUndoCell(String oldComment, String newComment, int[] previousSubtiles, int[] currentSubtiles, int currentTile) {
        super(0, 0, currentTile, 0);
        typeModif = TypeModif.Everything;
        previousComment = oldComment;
        comment = newComment;
        System.arraycopy(previousSubtiles, 0, this.previousSubtiles, 0,
                previousSubtiles.length);
        System.arraycopy(currentSubtiles, 0, this.currentSubtiles, 0,
                currentSubtiles.length);
    }


    public void undoTileChanges(int[] tuiles, String[] commentaireTuile) {
        if (typeModif == TypeModif.SubTile)
            tuiles[getI()*4+getJ()] = getPreviousValue();
        else if (typeModif == TypeModif.Comment)
            commentaireTuile[getI()] = previousComment;
        else {
            commentaireTuile[getI()] = previousComment;
            System.arraycopy(this.previousSubtiles, 0, tuiles, getI()*4,
                    this.previousSubtiles.length);
        }
    }

    public void redoTileChanges(int[] tuiles, String[] commentaireTuile) {
        if (typeModif == TypeModif.SubTile)
            tuiles[getI()*4+getJ()] = getValue();
        else if (typeModif == TypeModif.Comment)
            commentaireTuile[getI()] = comment;
        else {
            commentaireTuile[getI()] = comment;
            System.arraycopy(this.currentSubtiles, 0,tuiles, getI()*4,
                    this.currentSubtiles.length);
        }
    }

}
