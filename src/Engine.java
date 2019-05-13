import java.util.Random;
import java.util.Scanner;

public class Engine {

    private static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);

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

    private int requestMoveUser() {
        System.out.println("Please select a move");
        String input = sc.next();
        while(true) {
            try {
                int x = BoardIO.getInt(input);
                if(Game.getPossibleMoves().contains(x)) {
                    return x;
                }
            } catch (Exception e) {
                System.out.println("That was an invalid move, try again");
                input = sc.next();
            }
        }
    }

    private int requestMoveCPU() {

    }


}
