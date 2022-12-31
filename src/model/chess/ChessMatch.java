package model.chess;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Piece;
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

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) throws BoardException {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturePiece = makeMove(source, target);

        return (ChessPiece) capturePiece;
    }

    private void validateSourcePosition(Position sourcePosition) throws BoardException {
        if (!board.thereIsAPiece(sourcePosition)) {
            throw new ChessException("There is no piece on source position");
        }

        if (!board.piece(sourcePosition).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for a chosen piece");
        }
    }

    private void validateTargetPosition(Position sourcePosition, Position targetPosition) throws BoardException {
        if (!board.piece(sourcePosition).possibleMove(targetPosition)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private Piece makeMove(Position source, Position target) throws BoardException {
        Piece piece = board.removePiece(source);
        Piece capturePiece = board.removePiece(target);
        board.placePiece(piece, target);

        return capturePiece;
    }


    private void placeNewPiece(char column, int row, ChessPiece piece) throws BoardException {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup() throws BoardException, ChessException {
        placeNewPiece('c', 1, new Rook(board, Color.WRITE));
        placeNewPiece('c', 2, new Rook(board, Color.WRITE));
        placeNewPiece('d', 2, new Rook(board, Color.WRITE));
        placeNewPiece('e', 2, new Rook(board, Color.WRITE));
        placeNewPiece('e', 1, new Rook(board, Color.WRITE));
        placeNewPiece('d', 1, new King(board, Color.WRITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));

    }

}
