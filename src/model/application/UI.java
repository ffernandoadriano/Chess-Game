package model.application;

import model.chess.ChessException;
import model.chess.ChessPiece;
import model.chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class UI {
    private UI() {
    }

    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    public static ChessPosition readChessPosition(Scanner sc) throws ChessException {
        try {
            String strPosition = sc.next();
            char column = strPosition.charAt(0);
            int row = Integer.parseInt(strPosition.substring(1));

            return new ChessPosition(column, row);

        } catch (RuntimeException e) {
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8.");
        }
    }

    public static void printBoard(ChessPiece[][] chessPieces) {
        for (int i = 0, j = chessPieces.length; i < chessPieces.length; i++, j--) {
            System.out.print(j + " ");
            for (int k = 0; k < chessPieces[i].length; k++) {
                printPiece(chessPieces[i][k]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece) {
        if (piece != null) {
            switch (piece.getColor()) {
                case WRITE -> System.out.print(ANSI_WHITE + piece + ANSI_RESET);
                case BLACK -> System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        } else {
            System.out.print("-");
        }
        System.out.print(" ");
    }

}
