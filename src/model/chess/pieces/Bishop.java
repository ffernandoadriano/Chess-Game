package model.chess.pieces;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Position;
import model.chess.ChessPiece;
import model.chess.Color;

public class Bishop extends ChessPiece {

    public Bishop(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "B";
    }

    @Override
    public boolean[][] possibleMoves() throws BoardException {
        boolean[][] auxMatrix = new boolean[getBoard().getRows()][getBoard().getColumns()]; //start all false

        Position auxPosition = new Position(0, 0);

        //Diagonal Above-Left
        auxPosition.setValues(position.getRow() - 1, position.getColumn() - 1);

        while (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            auxPosition.setValues(auxPosition.getRow() - 1, auxPosition.getColumn() - 1);
        }

        if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Above-Right
        auxPosition.setValues(position.getRow() - 1, position.getColumn() + 1);

        while (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            auxPosition.setValues(auxPosition.getRow() - 1, auxPosition.getColumn() + 1);
        }

        if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Below-Left
        auxPosition.setValues(position.getRow() + 1, position.getColumn() - 1);

        while (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            auxPosition.setValues(auxPosition.getRow() + 1, auxPosition.getColumn() - 1);
        }

        if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Diagonal Below-Right
        auxPosition.setValues(position.getRow() + 1, position.getColumn() + 1);

        while (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            auxPosition.setValues(auxPosition.getRow() + 1, auxPosition.getColumn() + 1);
        }

        if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }

        return auxMatrix;
    }
}
