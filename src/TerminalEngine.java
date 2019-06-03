import java.util.List;
import java.util.Scanner;

public class TerminalEngine extends Engine {

    private static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        while(!checkGameOver()) {
            BoardIO.printBoard();
            BoardIO.printPossibleMoves();
            registerUserMove(requestUserMove());
            BoardIO.printBoard();
            if(checkGameOver()) {
                System.out.println("Winner " + getWinner());
            }
            registerCPUMove(requestCPUMove());
        }
        System.out.println("Winner " + getWinner());
    }
    /*
    game flow:

    ask player to choose X or O
    ask player to choose playing first or second

    while(game not over) {
        ask player 1 for a move
        check win
        ask player 2 for a move
        check win
    }
    */

    public static int requestUserMove() {
        System.out.println("Please select a move");
        String input = sc.next();
        List<Integer> possibleMoves = Game.getPossibleMoves();
        while(true) {
            try {
                int x = BoardIO.getInt(input);
                if(possibleMoves.contains(x)) {
                    return x;
                }
            } catch (Exception e) {
                System.out.println("That was an invalid move, try again");
                input = sc.next();
            }
        }
    }

    public static int requestCPUMove() {
        List<Integer> possibleMoves = Game.getPossibleMoves();
        return possibleMoves.get((int)(Math.random() * (double)possibleMoves.size()));
    }
}
