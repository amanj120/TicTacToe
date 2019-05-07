import java.util.ArrayList;
import java.util.List;

public class Game {
    public static int[] board = new int[92];
    //indeces 1-81 are the individual 81 pieces of the board
    //indeces 82-90 store the 9 3x3 boards
    //index 91 stores the winner of the game (if any)
    //index 0 says the index of the last move played

    /**
     * Given a borad index (0-8 for the actual 81 piece game, 9 for the underlying 3x3 game)
     * This method checks if that board number has a player that won, and in the case that it does,
     * it updates the underlying board
     * @param boardNum the board number being checked
     */
    public static void checkWin(int boardNum) {
        int start = boardNum * 9 + 1;
        int[][] mini = new int[3][3];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                mini[i][j] = board[start + i*3 + j];
            }
        }
        int winner = 0;
        //rows
        for(int i = 0; i < 3; i++) {
            if(mini[i][0] != 0) {
                if(mini[i][0] == mini[i][1] && mini[i][1] == mini[i][2]) {
                    winner = mini[i][0];
                }
            }
        }
        //columns
        for(int i = 0; i < 3; i++) {
            if(mini[0][i] != 0) {
                if(mini[0][i] == mini[1][i] && mini[1][i] == mini[2][i]) {
                    winner = mini[i][0];
                }
            }
        }
        //diagonals
        if(mini[0][0] != 0 && mini[0][0] == mini[1][1] && mini[1][1] == mini[2][2]) {
            winner = mini[0][0];
        }
        if(mini[2][0] != 0 && mini[2][0] == mini[1][1] && mini[1][1] == mini[0][2]) {
            winner = mini[2][0];
        }
        if(winner != 0) {
            board[82 + boardNum] = winner;
        }
    }

    /**
     * When the player (represented by a 1 in the board) selects an index to place the piece at,
     * the board updates and checks for potential wins
     * @param index
     */
    public static void registerPlayerMove(int index) {
        if (board[index] == 0) {
            board[index] = 1;
            board[0] = index;
            int beforeChecking = board[parent(index)];
            checkWin((index-1)/9);
            if (board[parent(index)] != beforeChecking) {
                checkWin(9);
            }
            if(board[91] != 0) {
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
    public List<Integer> getPossibleMoves() {
        int lastMove = board[0];
        List<Integer> ret = new ArrayList<>();
        if(board[parent(lastMove)] != 0) {
            for(int i = 1; i <=81; i++) {
                if(board[i] == 0) {
                    ret.add(i);
                }
            }
        } else {
            int start = smaller(lastMove);
            for(int i = start; i < start + 9; i++) {
                if(board[i] == 0) {
                    ret.add(i);
                }
            }
        }
        return ret;
    }

    /**
     * given an index, it returns the index of parent board it belongs to
     */
    public static int parent(int n) {
        return ((n-1)/9) + 82;
    }

    /**
     * given an index of a piece, it returns the start of
     * the board it corresponds to
     * @param n
     * @return
     */
    public static int smaller(int n) {
        return (((n-1) % 9) * 9) + 1;
    }


}
