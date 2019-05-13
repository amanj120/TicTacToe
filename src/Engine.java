public interface Engine {

    int requestMoveUser();
    default int requestMoveCPU() {
        //TODO fill this out
        return 0;
    }

    default void registerUserMove() {
        Game.move(requestMoveUser());
    }

    default void registerCPUMove() {
        Game.move(requestMoveCPU());
    }

    default boolean checkGameOver() {
        return Game.checkGame();
    }

    default int getWinner() {
        return Game.getWinner();
    }
}
