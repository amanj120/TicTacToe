import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


class GameLight {

    /**
     * holds all the information of the game
     * index 0-80 hold the actual 9x9 board
     * indices 81-89 holds the underlying 3x3 board
     * index 90 holds the winner of the game (if there is one)
     * index 91 holds the number of moves played
     * index 92 holds the last move played
     * arbitrarily: 1 == X (><), 2 == O (<>)
     */

    private static final int NUM_MOVES = 91;
    private static final int LAST_MOVE = 92;
    private static final Random random = new Random();

    private static byte check3x3board(int boardIdx, byte[] board) {
        if(board[boardIdx + 81] != 0) {
            return board[boardIdx + 81];
        }
        int start = boardIdx * 9;
        for(int i = start; i < start+9; i+=3) {
            if(board[i] != 0 && board[i] == board[i + 1] && board[i + 1] == board[i + 2]) {
                return board[i];
            }
        }
        for(int i = start; i < start+3; i++) {
            if(board[i] != 0 && board[i] == board[i + 3] && board[i+3] == board[i+6]) {
                return board[i];
            }
        }
        if(board[start] != 0 && board[start] == board[start + 4] && board[start + 4] == board[start + 8]) {
            return board[start];
        }
        if(board[start + 2] != 0 && board[start + 2] == board[start + 4] && board[start + 4] == board[start + 6]){
            return board[start + 2];
        }
        return 0;
    }

    private static int checkGameOver(byte[] board) {
        return check3x3board(9, board);
    }

    public static void move(int index, byte[] board) {
        if(board[index] == 0) {
            board[index] = (byte)((board[NUM_MOVES] % 2) + 1);
            board[NUM_MOVES]++;
            board[LAST_MOVE] = (byte) index;
            board[parent((byte)index)] = check3x3board((index/9), board);
            board[90] = (byte)checkGameOver(board);
        } else {
            throw new IllegalArgumentException("not allowed dog");
        }
    }

    /** given the last move played by the player, this method generates a list of
     * indeces where the machine can play a piece
     * @return a list of possible net indeces a piece can be played at
     */
    public static List<Integer> getPossibleMoves(byte[] board) {
        List<Integer> ret = new ArrayList<>();
        byte last = board[LAST_MOVE];
        if ((board[NUM_MOVES] == 0)|| (board[parent(last)] != 0) || (board[parent(send(last))] != 0)) {
            for (int i = 0; i < 81; i++) {
                if(board[i] == 0){
                    ret.add(i);
                }
            }
            return ret;
        } else {
            int start = send(last);
            for (int i = start; i < start + 9; i++) {
                if (board[i] == 0) {
                    ret.add(i);
                }
            }
            return ret;
        }
    }

    /**
     * given an index, it returns the index of parent board it belongs to
     * precondition: n < 81
     */
    private static byte parent(byte n) {
        return (byte) ((n/9) + 81);
    }

    /**
     * finds the index of the next move
     * precondition: n < 81
     */
    private static byte send(byte n) {
        return (byte)((n%9) * 9);
    }

    /**
     * simulates a full game
     * @return 1 if the computer won, 2 if the player won
     */
    private static int simulate(byte[] board) {
        if(board[90] != 0) {
            return board[90];
        } else {
            List<Integer> moves = getPossibleMoves(board);
            if(moves.size() == 0) { //tied game
                return -1;
            }
            int nextIdx = random.nextInt(moves.size());
            move(moves.get(nextIdx), board);
            return simulate(board);
        }
    }

    /**
     * simulates a full game given a move to make
     * reverts the move at the ends
     * @param m the index of the move to make
     * @return -1 if the simulation ends in a tie, 1 if X wins 2 if O wins
     */
    public static int simulate(int m, byte[] board) {
        movePrint(m, board);
        return simulate(board);
    }

    public static void movePrint(int move, byte[] board) {
        System.out.println("moving " + BoardIO.getSeq(move));
        move(move, board);
        BoardIO.printBoard(board);
        BoardIO.printPossibleMoves(board);
        System.out.println(Arrays.toString(board));
        System.out.println();
    }

    public static void main(String[] args) {
        byte[] board = new byte[93];
        movePrint(8, board);
        movePrint(72, board);
        movePrint(0, board);
        //System.out.println(simulate(3, board));

        ArrayList<pair> pairs = new ArrayList<>();

        List<Integer> nextMoves = getPossibleMoves(board);
        for(int i : nextMoves) {
            byte[] copy = Arrays.copyOf(board, 93);
            move(i, copy);
            List<Integer> doubleMoves = getPossibleMoves(copy);
            for(int j : doubleMoves) {
                pairs.add(new pair(i, j));
            }
        }
        for (pair p : pairs) {
            int ties = 0;
            int wins = 0;
            int losses = 0;

            int first = p.x;
            int second = p.y;

            for(int i = 0; i < 10000; i++) {
                byte[] copy = Arrays.copyOf(board, 93);
                move(first, copy);
                move(second, copy);
                int result = simulate(copy);
                if(result == -1) {
                    ties++;
                } else if (result == 1) {
                    losses++;
                } else {
                    wins++;
                }
            }
            System.out.println(p.toString() + " wi: " + wins + " ti: " + ties + " lo: " + losses);
        }


    }


    private static class pair {
        int x;
        int y;

        private pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
        void setX(int x) {
            this.x = x;
        }

        void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }
}
