package model.chess.pieces;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Position;
import model.chess.ChessMatch;
import model.chess.ChessPiece;
import model.chess.Color;

public class King extends ChessPiece {

    private ChessMatch chessMatch; // association

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean canMove(Position position) throws BoardException {
        ChessPiece chessPiece = (ChessPiece) getBoard().piece(position);
        return chessPiece == null || chessPiece.getColor() != getColor();
    }

    private boolean testRookCastling(Position position) throws BoardException {
        ChessPiece chessPiece = (ChessPiece) getBoard().piece(position);
        return chessPiece instanceof Rook && chessPiece.getColor() == getColor() && chessPiece.getMoveCount() == 0;
    }

    @Override
    public boolean[][] possibleMoves() throws BoardException {
        boolean[][] auxMatrix = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position auxPosition = new Position(0, 0);

        auxPosition.setValues(position.getRow() - 1, position.getColumn());

        //Above
        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Below
        auxPosition.setValues(position.getRow() + 1, position.getColumn());

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Left
        auxPosition.setValues(position.getRow(), position.getColumn() - 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Right
        auxPosition.setValues(position.getRow(), position.getColumn() + 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;

        }


        //Diagonal Above-Left
        auxPosition.setValues(position.getRow() - 1, position.getColumn() - 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Above-Right
        auxPosition.setValues(position.getRow() - 1, position.getColumn() + 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Below-Left
        auxPosition.setValues(position.getRow() + 1, position.getColumn() - 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Above-Right
        auxPosition.setValues(position.getRow() + 1, position.getColumn() + 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        // ## SpecialMove Castling right side##
        if (getMoveCount() == 0 && !chessMatch.isCheck()) {

            Position auxRook = new Position(position.getRow(), position.getColumn() + 3); // default position of right turret

            if (testRookCastling(auxRook)) {
                Position auxP1 = new Position(position.getRow(), position.getColumn() + 1);
                Position auxP2 = new Position(position.getRow(), position.getColumn() + 2);

                /*check for null positions on the right side between king and rook*/
                if (!getBoard().thereIsAPiece(auxP1) && !getBoard().thereIsAPiece(auxP2)) {
                    auxMatrix[position.getRow()][position.getColumn() + 2] = true;
                }
            }
        }


        // ## SpecialMove Castling left side ##
        if (getMoveCount() == 0 && !chessMatch.isCheck()) {

            Position auxRook = new Position(position.getRow(), position.getColumn() - 4); // default position of left turret

            if (testRookCastling(auxRook)) {
                Position auxP1 = new Position(position.getRow(), position.getColumn() - 1);
                Position auxP2 = new Position(position.getRow(), position.getColumn() - 2);
                Position auxP3 = new Position(position.getRow(), position.getColumn() - 3);

                /*check for null positions on the left side between king and rook*/
                if (!getBoard().thereIsAPiece(auxP1) && !getBoard().thereIsAPiece(auxP2) && !getBoard().thereIsAPiece(auxP3)) {
                    auxMatrix[position.getRow()][position.getColumn() - 2] = true;
                }
            }
        }

        return auxMatrix;
    }
}
