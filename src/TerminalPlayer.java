import java.util.List;
import java.util.Scanner;

public class TerminalPlayer {

    private static Scanner sc;
    private static BoardIO bio = new BoardIO();
    private static final int PLAYER_MOVE_NUMBER = 93; // is the CPU the first or second player to move
    private static final int BOARD_LENGTH = 94;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        byte[] board = new byte[BOARD_LENGTH];

        Game.registerPlayerMoveNumber(board, getPlayerMoveNumber());

        bio.printBoard(board);
        if(!Game.playerMovesFirst(board)) {//optimal move for the CPU to play on the first move
            movePrint(40, board);
        }

        while(true) {
            int userMove = registerUserMove(Game.getPossibleMoves(board));

            movePrint(userMove, board);

            if(Game.isGameOver(board)) {
                break;
            }

            int cpuMove = Game.registerCPUMove(board);

            movePrint(cpuMove, board);

            if(Game.isGameOver(board))
                break;
        }

        System.out.println("Game over yeet");
    }



    public static byte getPlayerMoveNumber() {
        System.out.println("Do you want to move first (1) or second (2)?");
        int player_move = sc.nextInt();
        while(player_move != 1 && player_move != 2) {
            System.out.println("not valid, choose 1 or 2");
            player_move = sc.nextInt();
        }
        return (byte) player_move;
    }

    public static int registerUserMove(List<Integer> possible) {
        System.out.println("enter your move below");
        int m;
        String input;
        do {
            input = sc.next();
            try {
                m = BoardIO.getInt(input);
            } catch (Exception e) {
                m = -1;
            }
        } while(!possible.contains(m));
        return m;
    }

    private static int registerRandomMove(List<Integer> possible) {
        return possible.get((int) (Math.random() * possible.size()));
    }

    static void movePrint(int move, byte[] board) {
        System.out.println("moving " + BoardIO.getSeq(move));
        Game.move(move, board);
        bio.printBoard(board);
        bio.printPossibleMoves(board);
        //System.out.println(Arrays.toString(board));
        System.out.println();
    }

}
