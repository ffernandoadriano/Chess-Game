package model.chess;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Position;
import model.chess.pieces.King;
import model.chess.pieces.Rook;

public class ChessMatch {

    private Board board;

    public ChessMatch() throws BoardException {
        board = new Board(8, 8);
        initialSetup();
    }

    public ChessPiece[][] getPieces() throws BoardException {
        ChessPiece[][] auxMatrix = new ChessPiece[board.getRows()][board.getColumns()];

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                auxMatrix[i][j] = (ChessPiece) board.piece(i, j);
            }
        }

        return auxMatrix;
    }

    private void initialSetup() throws BoardException {
        board.placePiece(new Rook(board, Color.WRITE), new Position(2, 1));
        board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
        board.placePiece(new King(board, Color.WRITE), new Position(7, 4));
    }

}
