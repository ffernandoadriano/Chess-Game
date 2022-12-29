package model.application;

import model.boardgame.BoardException;
import model.chess.ChessMatch;

public class Application {
    public static void main(String[] args) {
        try {
            ChessMatch chessMatch = new ChessMatch();
            UI.printBoard(chessMatch.getPieces()); //User Interface - UI

        } catch (BoardException e) {
            System.out.println(e.getMessage());
        }

    }
}