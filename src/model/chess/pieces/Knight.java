package model.chess.pieces;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Position;
import model.chess.ChessPiece;
import model.chess.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "N";
    }

    private boolean canMove(Position position) throws BoardException {
        ChessPiece chessPiece = (ChessPiece) getBoard().piece(position);
        return chessPiece == null || chessPiece.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() throws BoardException {
        boolean[][] auxMatrix = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position auxPosition = new Position(0, 0);

        //Diagonal Above-Left
        auxPosition.setValues(position.getRow() - 2, position.getColumn() - 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Above-Left-Left
        auxPosition.setValues(position.getRow() - 1, position.getColumn() - 2);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Above-Right
        auxPosition.setValues(position.getRow() - 2, position.getColumn() + 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Above-Right-Right
        auxPosition.setValues(position.getRow() - 1, position.getColumn() + 2);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Below-Left
        auxPosition.setValues(position.getRow() + 2, position.getColumn() - 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Below-Left-Left
        auxPosition.setValues(position.getRow() + 1, position.getColumn() - 2);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Below-Right
        auxPosition.setValues(position.getRow() + 2, position.getColumn() + 1);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Below-Right-Right
        auxPosition.setValues(position.getRow() + 1, position.getColumn() + 2);

        if (getBoard().positionExists(auxPosition) && canMove(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }

        return auxMatrix;
    }
}
