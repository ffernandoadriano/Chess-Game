package model.chess;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Piece;
import model.boardgame.Position;
import model.chess.pieces.Bishop;
import model.chess.pieces.King;
import model.chess.pieces.Knight;
import model.chess.pieces.Pawn;
import model.chess.pieces.Queen;
import model.chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

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

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
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

        ChessPiece movPiece = (ChessPiece) board.piece(target);

        /* ## specialMove promotion ##  */
        promoted = null; //it is always necessary to clear the promotion so as not to conflict with the previous ones

        if (movPiece instanceof Pawn) {
            if ((movPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        check = testCheck(opponent(currentPlayer));

        if (!testCheckmate(opponent(currentPlayer))) {
            nextTurn();
        }

        /* ## specialMove en passant ##  */
        if (movPiece instanceof Pawn && (target.getRow() == source.getRow() + 2 || target.getRow() == source.getRow() - 2)) {
            enPassantVulnerable = movPiece;
        }

        return (ChessPiece) capturePiece;
    }

    public ChessPiece replacePromotedPiece(String type) throws BoardException {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }

        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            return promoted;
        }

        Position promoPosition = promoted.getChessPosition().toPosition();
        Piece piece = board.removePiece(promoPosition);
        piecesOnTheBoardList.remove(piece);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, promoPosition);
        piecesOnTheBoardList.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        return switch (type) {
            case "B" -> new Bishop(board, color);
            case "N" -> new Knight(board, color);
            case "R" -> new Rook(board, color);
            default -> new Queen(board, color);
        };
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

        // ## SpecialMove Castling right side##
        if (piece instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position rookSource = new Position(source.getRow(), source.getColumn() + 3); // default position of right turret
            Position rookTarget = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rookPiece = (ChessPiece) board.removePiece(rookSource);
            board.placePiece(rookPiece, rookTarget);
            rookPiece.increaseMoveCount();
        }

        // ## SpecialMove Castling left side##
        if (piece instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position rookSource = new Position(source.getRow(), source.getColumn() - 4); // default position of left turret
            Position rookTarget = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rookPiece = (ChessPiece) board.removePiece(rookSource);
            board.placePiece(rookPiece, rookTarget);
            rookPiece.increaseMoveCount();
        }

        // ## SpecialMove en passant##
        if (piece instanceof Pawn) {

            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;

                if (piece.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }

                capturedPiece = board.removePiece(pawnPosition);
                piecesOnTheBoardList.remove(capturedPiece);
                capturedPiecesList.add(capturedPiece);
            }
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) throws BoardException {
        ChessPiece piece = (ChessPiece) board.removePiece(target);
        piece.decreaseMoveCount();
        board.placePiece(piece, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPiecesList.remove(capturedPiece);
            piecesOnTheBoardList.add(capturedPiece);
        }

        // ## SpecialMove Castling right side##
        if (piece instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position rookSource = new Position(source.getRow(), source.getColumn() + 3); // default position of right turret
            Position rookTarget = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rookPiece = (ChessPiece) board.removePiece(rookTarget);
            board.placePiece(rookPiece, rookSource);
            rookPiece.decreaseMoveCount();
        }

        // ## SpecialMove Castling left side##
        if (piece instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position rookSource = new Position(source.getRow(), source.getColumn() - 4); // default position of left turret
            Position rookTarget = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rookPiece = (ChessPiece) board.removePiece(rookTarget);
            board.placePiece(rookPiece, rookSource);
            rookPiece.decreaseMoveCount();
        }

        // ## SpecialMove en passant##
        if (piece instanceof Pawn) {

            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawnPiece = (ChessPiece) board.removePiece(target); //was placed in the wrong default position because of the en passant
                Position pawnPosition;

                if (piece.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }

                board.placePiece(pawnPiece, pawnPosition);
            }
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
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));


        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }

}
