package model.chess.pieces;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Position;
import model.chess.ChessMatch;
import model.chess.ChessPiece;
import model.chess.Color;

public class Pawn extends ChessPiece {

    private ChessMatch chessMatch; // association

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public boolean[][] possibleMoves() throws BoardException {
        boolean[][] auxMatrix = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position auxPosition = new Position(0, 0);
        Position auxPosition2 = new Position(0, 0);

        //White piece rules
        if (getColor() == Color.WHITE) {
            auxPosition.setValues(position.getRow() - 2, position.getColumn());
            auxPosition2.setValues(position.getRow() - 1, position.getColumn());

            //walk two houses
            if (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition) && getBoard().positionExists(auxPosition2) && !getBoard().thereIsAPiece(auxPosition2) && getMoveCount() == 0) {
                auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            }

            //walk a house
            if (getBoard().positionExists(auxPosition2) && !getBoard().thereIsAPiece(auxPosition2)) {
                auxMatrix[auxPosition2.getRow()][auxPosition2.getColumn()] = true;
            }

            auxPosition.setValues(position.getRow() - 1, position.getColumn() - 1);
            // capture piece on the left
            if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
                auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            }

            auxPosition.setValues(position.getRow() - 1, position.getColumn() + 1);
            // capture piece on the right
            if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
                auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            }


            // ## SpecialMove en passant ##
            if (position.getRow() == 3) {
                // Rule left side
                Position left = new Position(position.getRow(), position.getColumn() - 1);

                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    auxMatrix[left.getRow() - 1][left.getColumn()] = true;
                }

                // Rule right side
                Position right = new Position(position.getRow(), position.getColumn() + 1);

                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    auxMatrix[right.getRow() - 1][right.getColumn()] = true;
                }
            }


        } else {
            //Black piece rules
            auxPosition.setValues(position.getRow() + 2, position.getColumn());
            auxPosition2.setValues(position.getRow() + 1, position.getColumn());

            //walk two houses
            if (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition) && getBoard().positionExists(auxPosition2) && !getBoard().thereIsAPiece(auxPosition2) && getMoveCount() == 0) {
                auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            }

            //walk a house
            if (getBoard().positionExists(auxPosition2) && !getBoard().thereIsAPiece(auxPosition2)) {
                auxMatrix[auxPosition2.getRow()][auxPosition2.getColumn()] = true;
            }

            // capture piece on the left
            auxPosition.setValues(position.getRow() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
                auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            }

            // capture piece on the right
            auxPosition.setValues(position.getRow() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
                auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            }


            // ## SpecialMove en passant ##
            if (position.getRow() == 4) {
                // Rule left side
                Position left = new Position(position.getRow(), position.getColumn() - 1);

                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    auxMatrix[left.getRow() + 1][left.getColumn()] = true;
                }

                // Rule right side
                Position right = new Position(position.getRow(), position.getColumn() + 1);

                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    auxMatrix[right.getRow() + 1][right.getColumn()] = true;
                }
            }
        }


        return auxMatrix;
    }
}
