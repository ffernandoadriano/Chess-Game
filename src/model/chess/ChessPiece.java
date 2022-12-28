package model.chess;

import model.boardgame.Board;
import model.boardgame.Piece;

public class ChessPiece extends Piece {
    private Color color;

    public ChessPiece() {
        super();
    }

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
