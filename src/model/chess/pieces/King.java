package model.chess.pieces;

import model.boardgame.Board;
import model.chess.ChessPiece;
import model.chess.Color;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] auxMatrix = new boolean[getBoard().getRows()][getBoard().getColumns()];
        return auxMatrix;
    }
}
