import java.util.ArrayList;
import java.util.List;

public class Game {

    /**
     * holds all the inforomation of the game
     * index 0-80 hold the actual 9x9 board
     * indices 81-89 holds the underlying 3x3 board
     * index 90 holds the winner of the game (if there is one)
     * index 91 holds the number of moves played
     * indices 92-172 holds the moves played thus far
     */
    private static int[] board = new int[173];

    private static final int NUM_MOVES = 91;


    /**
     * checks the rows of the last move played
     */
    private static boolean checkRows(int start) {
        for(int i = start; i < start+9; i+=3) {
            if(board[start] != 0 && board[start] == board[start + 1] && board[start+1] == board[start+2]) {
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
            if(board[start] != 0 && board[start] == board[start + 3] && board[start+3] == board[start+6]) {
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

    private static int getIndexOfLastMove() {
        return NUM_MOVES + 91;
    }

    /**
     * Given a borad index (0-8 for the actual 81 piece game, 9 for the underlying 3x3 game)
     * This method checks if that board number has a player that won, and in the case that it does,
     * it updates the underlying board
     */
    private static void checkWin() {
        int last = getIndexOfLastMove();
        int start = begin(last);
        if(checkCols(start) || checkDiag(start) || checkRows(start)) {
            board[parent(start)] = board[last];
            if(checkCols(81) || checkDiag(81) || checkRows(81)) {
                board[90] = board[parent(start)];
            }
        }
    }


    /**
     * When the player (represented by a 1 in the board) selects an index to place the piece at,
     * the board updates and checks for potential wins
     * @param index the index of the move being played
     */
    public static void move(int index) {
        if (board[index] == 0) {
            board[NUM_MOVES]++;
            board[index] = (board[NUM_MOVES] % 2) + 1;
            board[getIndexOfLastMove()] = index;
            checkWin();
            if(board[90] != 0) {
                System.out.println("Game Over");
            }
        } else {
            throw new IllegalArgumentException("That index already has another player's piece on it");
        }
    }

    /** given the last move played by the player, this method generates a list of
     * indeces where the machine can play a piece
     * @return a list of possible net indeces a piece can be played at
     */
    public static List<Integer> getPossibleMoves(int[] board) {
        int lastMove = getIndexOfLastMove();
        List<Integer> ret = new ArrayList<>();
        if(board[parent(lastMove)] != 0) {
            for(int i = 1; i <=81; i++) {
                if(board[i] == 0) {
                    ret.add(i);
                }
            }
        } else {
            int start = begin(lastMove);
            for(int i = start; i < start + 9; i++) {
                if(board[i] == 0) {
                    ret.add(i);
                }
            }
        }
        return ret;
    }

    public static void revert() {
        int last = getIndexOfLastMove();
        board[last] = 0;
        board[parent(last)] = 0;
        board[90] = 0;
        checkWin();
        board[NUM_MOVES]--;
        board[last] = 0;
    }

    /**
     * given an index, it returns the index of parent board it belongs to
     */
    private static int parent(int n) {
        return ((n)/9) + 81;
    }

    /**
     * returns the beginning of the 3x3 board given an index on the board
     */
    private static int begin(int n) {
        if(n >= 90) {
            throw new IllegalArgumentException("You can't call begin on an index higher than 89");
        } else {
            return ((n/9) * 9);
        }
    }

    /**
     * resets the board
     */
    private static void init() {
        board = new int[92];
    }

    public static void main(String[] args) {
        init();
        PrintBoard.print(board);
    }


}
