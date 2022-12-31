package model.boardgame;

public abstract class Piece {
    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
    }

    protected Board getBoard() {
        return board;
    }

    public abstract boolean[][] possibleMoves() throws BoardException;


    //** Hook Methods **
    //https://pt.coursera.org/lecture/desenvolvimento-agil-com-padroes-de-projeto/hook-methods-9VqFy
    public boolean possibleMove(Position position) throws BoardException {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    //** Hook Methods **
    public boolean isThereAnyPossibleMove() throws BoardException {
        boolean[][] auxMatrix = possibleMoves();

        for (int i = 0; i < auxMatrix.length; i++) {
            for (int j = 0; j < auxMatrix[i].length; j++) {
                if (auxMatrix[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

}
