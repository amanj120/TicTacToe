abstract class Engine {

    static int requestUserMove() {return 0;}
    static int requestCPUMove() {return 0;}


    static void registerUserMove(int move) {
        Game.move(move);
    }

    static void registerCPUMove(int move) {
        Game.move(move);
    }

    static boolean checkGameOver() {
        return Game.checkGame();
    }

    static int getWinner() {
        return Game.getWinner();
    }
}
