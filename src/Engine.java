public abstract class Engine {

    static int requestUserMove() {return 0;}
    static int requestCPUMove() {return 0;}


    public static void registerUserMove(int move) {
        Game.move(move);
    }

    public static void registerCPUMove(int move) {
        Game.move(move);
    }

    public static boolean checkGameOver() {
        return Game.checkGame();
    }

    public static int getWinner() {
        return Game.getWinner();
    }
}
