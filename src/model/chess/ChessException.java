package model.chess;

import model.boardgame.BoardException;

public class ChessException extends BoardException {
    public ChessException(String message) {
        super(message);
    }
}
