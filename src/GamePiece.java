import java.util.Optional;

public class GamePiece<T>{

    private Optional<T> piece = Optional.empty();

    public GamePiece() {
        piece = Optional.empty();
    }
    public GamePiece(T t){
        piece = Optional.of(t);
    }


}