package model.chess;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Piece;
import model.boardgame.Position;
import model.chess.pieces.King;
import model.chess.pieces.Pawn;
import model.chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;

    private final Board board; // association
    private final List<Piece> piecesOnTheBoardList = new ArrayList<>();
    private final List<Piece> capturedPiecesList = new ArrayList<>();

    public ChessMatch() throws BoardException {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
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

    public boolean[][] possibleMoves(ChessPosition sourcePosition) throws BoardException {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) throws BoardException {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturePiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturePiece);
            throw new ChessException("You can't put yourself in check");
        }

        check = testCheck(opponent(currentPlayer));

        if (!testCheckmate(opponent(currentPlayer))) {
            nextTurn();
        }

        return (ChessPiece) capturePiece;
    }

    private void validateSourcePosition(Position sourcePosition) throws BoardException {
        if (!board.thereIsAPiece(sourcePosition)) {
            throw new ChessException("There is no piece on source position");
        }

        if (currentPlayer != ((ChessPiece) board.piece(sourcePosition)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
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
        ChessPiece piece = (ChessPiece) board.removePiece(source);
        piece.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(piece, target);

        if (capturedPiece != null) {
            piecesOnTheBoardList.remove(capturedPiece);
            capturedPiecesList.add(capturedPiece);
        }

        return capturedPiece;
    }

    private void undoMove(Position sourcePosition, Position targetPosition, Piece capturedPiece) throws BoardException {
        ChessPiece piece = (ChessPiece) board.removePiece(targetPosition);
        piece.decreaseMoveCount();
        board.placePiece(piece, sourcePosition);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, targetPosition);
            capturedPiecesList.remove(capturedPiece);
            piecesOnTheBoardList.add(capturedPiece);
        }
    }

    private void nextTurn() {
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
        turn++;
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> pieces = piecesOnTheBoardList.stream().filter(x -> ((ChessPiece) x).getColor() == color).toList();

        for (Piece piece : pieces) {
            if (piece instanceof King) {
                return (ChessPiece) piece;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    private boolean testCheck(Color color) throws BoardException {
        Position kingPosition = king(color).getChessPosition().toPosition();

        List<Piece> opponentPiecesList = piecesOnTheBoardList.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).toList();

        for (Piece piece : opponentPiecesList) {
            boolean[][] auxPossibleMoves = piece.possibleMoves();
            if (auxPossibleMoves[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }

        return false;
    }

    private boolean testCheckmate(Color color) throws BoardException {
        if (!check) {
            return false;
        }

        List<Piece> pieceList = piecesOnTheBoardList.stream().filter(x -> ((ChessPiece) x).getColor() == color).toList();

        for (Piece piece : pieceList) {
            boolean[][] auxMatrixPossibleMove = piece.possibleMoves();

            for (int i = 0; i < auxMatrixPossibleMove.length; i++) {
                for (int j = 0; j < auxMatrixPossibleMove[i].length; j++) {

                    if (auxMatrixPossibleMove[i][j]) { // possible moment to protect the king
                        Position source = ((ChessPiece) piece).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        checkMate = testCheck(color);
                        undoMove(source, target, capturedPiece);

                        if (!checkMate) {
                            return false;
                        }

                    }
                }
            }
        }

        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) throws BoardException {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoardList.add(piece);
    }

    private void initialSetup() throws BoardException {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
    }

}
