import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game {

    /**
     * holds all the inforomation of the game
     * index 0-80 hold the actual 9x9 board
     * indices 81-89 holds the underlying 3x3 board
     * index 90 holds the winner of the game (if there is one)
     * index 91 holds the number of moves played
     * indices 92-172 holds the moves played thus far
     * arbitrarily: 1 == X (><), 2 == O (<>)
     */
    static int[] board = new int[173];
    private static Random random = new Random();
    private static final int NUM_MOVES = 91;

    /**
     * checks the rows of the last move played
     */
    private static boolean checkRows(int start) {
        for(int i = start; i < start+9; i+=3) {
            if(board[i] != 0 && board[i] == board[i + 1] && board[i + 1] == board[i + 2]) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks the columns of the last move played
     */
    private static boolean checkCols(int start) {
        for(int i = start; i < start+3; i++) {
            if(board[i] != 0 && board[i] == board[i + 3] && board[i+3] == board[i+6]) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks the columns of the last move played
     */
    private static boolean checkDiag(int s) {
        if(board[s] != 0 && board[s] == board[s + 4] && board[s + 4] == board[s + 8]) {
            return true;
        } else if (board[s + 2] != 0 && board[s + 2] == board[s + 4] && board[s + 4] == board[s + 6]) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * board[getIndexOfLastMove()] will return the index of the last move
     * board[board[getIndexOfLastMove()]] will return the piece placed at the last move
     * @return the index of the board that contains the index of the last move played
     */
    private static int getIndexOfLastMove() {
        return board[NUM_MOVES] + 91;
    }

    /**
     * Given a borad index (0-8 for the actual 81 piece game, 9 for the underlying 3x3 game)
     * This method checks if that board number has a player that won, and in the case that it does,
     * it updates the underlying board
     */
    private static void checkWin() {
        int last = getIndexOfLastMove();
        int start = begin(board[last]);
        if(board[parent(start)] == 0) {
            if(checkCols(start) || checkDiag(start) || checkRows(start)) {
                board[parent(start)] = board[board[last]];
                if (checkCols(81) || checkDiag(81) || checkRows(81)) {
                    board[90] = board[parent(start)];
                }
            }
        }
    }

    public static boolean checkGame() {
        return board[90] != 0;
    }

    public static int getWinner() {
        return board[90];
    }
    /**
     * When the player (represented by a 1 in the board) selects an index to place the piece at,
     * the board updates and checks for potential wins
     * @param index the index of the move being played
     */
    public static void move(int index) {
        if (board[index] == 0) {
            board[NUM_MOVES]++; //increases number of moves
            board[index] = (board[NUM_MOVES] % 2) + 1; //sets it to not 0
            board[getIndexOfLastMove()] = index; //sets the log of moves played
            checkWin();
        } else {
            throw new IllegalArgumentException("That index already has another player's piece on it");
        }
    }

    /** given the last move played by the player, this method generates a list of
     * indeces where the machine can play a piece
     * @return a list of possible net indeces a piece can be played at
     */
    public static List<Integer> getPossibleMoves() {
        int last = getIndexOfLastMove();
        List<Integer> ret = new ArrayList<>();
        if(board[parent(board[last])] != 0 || board[parent(send(board[last]))] != 0) {
            for(int i = 0; i < 81; i++) {
                if(board[i] == 0) {
                    ret.add(i);
                }
            }
        } else {
            int start = send(board[last]);
            for(int i = start; i < start + 9; i++) {
                if(board[i] == 0) {
                    ret.add(i);
                }
            }
        }
        return ret;
    }

    private static void revert() {
        int last = getIndexOfLastMove();
        board[NUM_MOVES]--;
        board[board[last]] = 0;
        board[parent(board[last])] = 0;
        board[90] = 0;
        checkWin();
        board[last] = 0;
    }

    /**
     * given an index, it returns the index of parent board it belongs to
     * precondition: n < 81
     */
    private static int parent(int n) {
        return (n/9) + 81;
    }

    /**
     * returns the beginning of the 3x3 board given an index on the board
     * precondition: n < 90
     */
    private static int begin(int n) {
        return ((n/9) * 9);
    }

    /**
     * finds the index of the next move
     * precondition: n < 81
     */
    private static int send(int n) {
        return (n%9) * 9;
    }

    /**
     * resets the board
     */
    public static void init() {
        board = new int[173];
    }

    /**
     * simulates a full game
     * @return 1 if the computer won, 0 if the player won
     */
    private static int simulate() {
        if(board[90] != 0) {
            return board[90];
        } else {
            List<Integer> moves = getPossibleMoves();
            if(moves.size() == 0) { //tied game
                return -1;
            }
            int nextIdx = random.nextInt(moves.size());
            move(moves.get(nextIdx));
            int x = simulate();
            revert();
            return x;
        }
    }

    /**
     * simulates a full game given a move to make
     * reverts the move at the ends
     * @param m the index of the move to make
     * @return -1 if the simulation ends in a tie, 1 if X wins 2 if O wins
     */
    public static int simulate(int m) {
        move(m);
        int ret = simulate();
        revert();
        return ret;
    }

    private static void movePrint(int n) {
        System.out.println(n + ":\t" + BoardIO.getSeq(n));
        move(n);
        BoardIO.print();
        System.out.println(Arrays.toString(board));
        System.out.println(getPossibleMoves().toString());
    }

    private static void revertPrint(){
        revert();
        BoardIO.print();
        System.out.println(getPossibleMoves().toString());
    }
}
