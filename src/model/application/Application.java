package model.application;

import model.boardgame.BoardException;
import model.chess.ChessException;
import model.chess.ChessMatch;
import model.chess.ChessPiece;
import model.chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            ChessMatch chessMatch = new ChessMatch();
            while (true) {
                try {
                    UI.clearScreen();
                    UI.printMatch(chessMatch); //User Interface - UI
                    System.out.println();

                    System.out.print("Source: ");
                    ChessPosition source = UI.readChessPosition(sc);

                    boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                    UI.clearScreen();
                    UI.printBoard(chessMatch.getPieces(), possibleMoves); //User Interface - UI
                    System.out.println();

                    System.out.print("Target: ");
                    ChessPosition target = UI.readChessPosition(sc);

                    ChessPiece capturePiece = chessMatch.performChessMove(source, target);

                } catch (ChessException | InputMismatchException e) {
                    System.out.println(e.getMessage());
                    sc.nextLine();
                }
            }
        } catch (BoardException e) {
            System.out.println(e.getMessage());
        }
    }
}