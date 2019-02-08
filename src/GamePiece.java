import java.util.Optional;

public class GamePiece<T>{

    private T piece;

    GamePiece() {
        piece = null;
    }
    public GamePiece(T t){
        piece = t;
    }
}