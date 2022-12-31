package model.chess;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Piece;
import model.boardgame.Position;

public abstract class ChessPiece extends Piece {
    private Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    protected boolean isThereOpponentPiece(Position position) throws BoardException {
        ChessPiece auxChessPiece = (ChessPiece) getBoard().piece(position);

        return auxChessPiece != null && auxChessPiece.getColor() != color;
    }

}
