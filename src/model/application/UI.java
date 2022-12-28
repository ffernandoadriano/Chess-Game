package model.application;

import model.chess.ChessPiece;

public final class UI {
    private UI() {
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
            System.out.print(piece);
        } else {
            System.out.print("-");
        }
        System.out.print(" ");
    }

}
