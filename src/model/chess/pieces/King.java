package model.chess.pieces;

import model.boardgame.Board;
import model.boardgame.BoardException;
import model.boardgame.Position;
import model.chess.ChessPiece;
import model.chess.Color;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean canMove(Position position) throws BoardException {
        ChessPiece chessPiece = (ChessPiece) getBoard().piece(position);
        return chessPiece == null || chessPiece.getColor() != getColor();
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

        return auxMatrix;
    }
}
