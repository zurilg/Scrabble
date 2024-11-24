public interface ScrabbleModelView {
    void handleLetterPlacement(ScrabbleEvent.TilePlacement e);
    void updateBoard();
}
