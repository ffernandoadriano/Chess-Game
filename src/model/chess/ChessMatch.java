package model.chess;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.chess.pieces.King;
import model.chess.pieces.Rook;

public class ChessMatch {

    private Board board;

    public ChessMatch() throws BoardException, ChessException {
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

    private void placeNewPiece(char column,int row, ChessPiece piece) throws ChessException, BoardException {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup() throws BoardException, ChessException {
        placeNewPiece('b', 6, new Rook(board, Color.WRITE));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('e', 1, new King(board, Color.BLACK));
    }

}
