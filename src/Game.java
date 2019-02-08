import java.util.Optional;
import java.util.ArrayList;

public interface Game<T> {

    GamePiece[][] board = new GamePiece[3][3];

    default GamePiece get (Coordinate coor) {
        return board[coor.x][coor.y];
    }

    default void place (GamePiece t, Coordinate coor) {
        if(get(coor) == null) {
            board[coor.x][coor.y] = t;
        } else {
            System.out.println("That square is already filled YEEEET");
        }
    }

    default ArrayList<Coordinate> possibleMoves() {
        return null;
    }
}

