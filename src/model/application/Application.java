package model.application;

import model.boardgame.BoardException;
import model.chess.ChessException;
import model.chess.ChessMatch;
import model.chess.ChessPiece;
import model.chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) throws BoardException {
        Scanner sc = new Scanner(System.in);
        List<ChessPiece> capturedPieceList = new ArrayList<>();
        ChessMatch chessMatch = new ChessMatch();

        while (!chessMatch.isCheckMate()) {
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch, capturedPieceList); //User Interface - UI
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

                if (capturePiece != null) {
                    capturedPieceList.add(capturePiece);
                }


                if (chessMatch.getPromoted() != null) {
                    System.out.print("Enter piece for promotion (B/N/R/Q): ");
                    String type = sc.next().toUpperCase();

                    while ((!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q"))) {
                        System.out.print("Invalid value! Enter piece for promotion (B/N/R/Q): ");
                        type = sc.next().toUpperCase();
                    }

                    chessMatch.replacePromotedPiece(type);
                }


            } catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }

        UI.clearScreen();
        UI.printMatch(chessMatch, capturedPieceList); //User Interface - UI
    }
}