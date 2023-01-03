package model.chess.pieces;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Position;
import model.chess.ChessPiece;
import model.chess.Color;

public class Rook extends ChessPiece {
    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public boolean[][] possibleMoves() throws BoardException {
        boolean[][] auxMatrix = new boolean[getBoard().getRows()][getBoard().getColumns()]; //start all false

        Position auxPosition = new Position(0, 0);

        //Above
        auxPosition.setValues(position.getRow() - 1, position.getColumn());

        while (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            auxPosition.setRow(auxPosition.getRow() - 1);
        }

        if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Below
        auxPosition.setValues(position.getRow() + 1, position.getColumn());

        while (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            auxPosition.setRow(auxPosition.getRow() + 1);
        }

        if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Left
        auxPosition.setValues(position.getRow(), position.getColumn() - 1);

        while (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            auxPosition.setColumn(auxPosition.getColumn() - 1);
        }

        if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }


        //Right
        auxPosition.setValues(position.getRow(), position.getColumn() + 1);

        while (getBoard().positionExists(auxPosition) && !getBoard().thereIsAPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
            auxPosition.setColumn(auxPosition.getColumn() + 1);
        }

        if (getBoard().positionExists(auxPosition) && isThereOpponentPiece(auxPosition)) {
            auxMatrix[auxPosition.getRow()][auxPosition.getColumn()] = true;
        }

        return auxMatrix;
    }
}
