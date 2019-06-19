import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import static java.util.Arrays.copyOf;

/*
https://en.wikipedia.org/wiki/Ultimate_tic-tac-toe
 */

class Game {

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
    private static Scanner sc = new Scanner(System.in);

    private static byte check3x3board(int boardIdx, byte[] board) {
        if(board[boardIdx + 81] != 0) {
            return board[boardIdx + 81];
        }
        int s = boardIdx * 9;
        if(board[s] != 0) { // upper left
            if(board[s] == board[s + 1] && board[s] == board[s + 2])
                return board[s];
            if(board[s] == board[s + 3] && board[s] == board[s + 6])
                return board[s];
        }
        s += 4;
        if (board[s] != 0) { // center
            if(board[s] == board[s + 1] && board[s] == board[s - 1])
                return board[s];
            if(board[s] == board[s + 2] && board[s] == board[s - 2])
                return board[s];
            if(board[s] == board[s + 3] && board[s] == board[s - 3])
                return board[s];
            if(board[s] == board[s + 4] && board[s] == board[s - 4])
                return board[s];
        }
        s += 4;
        if (board[s] != 0) { //lower right
            if(board[s] == board[s - 1] && board[s] == board[s - 2])
                return board[s];
            if(board[s] == board[s - 3] && board[s] == board[s - 6])
                return board[s];
        }
        return 0;
    }

    private static byte checkGameOver(byte[] board) {
        return check3x3board(9, board);
    }

    static void move(int index, byte[] board) {
        if(board[index] == 0) {
            board[index] = (byte)((board[NUM_MOVES] % 2) + 1);
            board[NUM_MOVES]++;
            board[LAST_MOVE] = (byte) index;
            int before = board[parent(index)];
            board[parent(index)] = check3x3board((index/9), board);
            if(before != board[parent(index)]) {
                board[90] = checkGameOver(board);
            }
        } else {
            throw new IllegalArgumentException("not a legal move: that space is taken");
        }
    }

    /** given the last move played by the player, this method generates a list of
     * indeces where the machine can play a piece
     * @return a list of possible net indeces a piece can be played at
     */
    static List<Integer> getPossibleMoves(byte[] board) {
        List<Integer> ret = new ArrayList<>();
        byte last = board[LAST_MOVE];
        if ((board[NUM_MOVES] == 0)|| (board[parent(send(last))] != 0)) {
            for (int i = 0; i < 81; i++) {
                if(board[i] == 0 && board[parent((byte)i)] == 0){
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
    private static int parent(int n) {
        return ((n/9) + 81);
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

    private static void movePrint(int move, byte[] board) {
        System.out.println("moving " + BoardIO.getSeq(move));
        move(move, board);
        BoardIO.printBoard(board);
        BoardIO.printPossibleMoves(board);
        //System.out.println(Arrays.toString(board));
        System.out.println();
    }

    public static void main(String[] args) {
        byte[] board = new byte[93];
        //movePrint(8, board);
        //movePrint(72, board);
        //EmovePrint(0, board);
//
//        movePrint(40, board);
//        movePrint(36, board);
//        movePrint(0, board);
//        System.out.println(getPossibleMoves(board));
//        movePrint(1, board);
        //System.out.println(simulate(3, board));
        movePrint(40, board);
        ArrayList<Integer> maxNumPairs = new ArrayList<>();


        for(int turns = 0; turns < 100; turns ++) {


            if(checkGameOver(board) != 0) {
                //Collections.sort(maxNumPairs);
                System.out.println(maxNumPairs.toString());
                System.out.println("Game over yeet");
                return;
            }

            short[] pairs = findAllPairs(board);
            short[] winprobs = new short[pairs.length];
            //System.out.println(pairs.toString());
            for (int k = 0; k < pairs.length; k++) {
                kernel(k, board, pairs, winprobs);
                //System.out.println(formattedPairs[k]/100 + "," + formattedPairs[k]%100 + ":\t" + winprobs[k]);
                if(k%(pairs.length/9) == 0)
                    System.out.print(k/(pairs.length/9)+1 + " ");
            }
            System.out.println();
            int[] minimax = new int[getPossibleMoves(board).size()];
            Arrays.fill(minimax, 10000000);
            for (int i = 0; i < pairs.length; i++) {
                int idx = getPossibleMoves(board).indexOf(pairs[i] / 100);
                minimax[idx] = Math.min(minimax[idx], winprobs[i]);
            }
            System.out.println(Arrays.toString(minimax));

            int max = 0;
            int idx = -1;
            for (int i = 0; i < minimax.length; i++) {
                if (minimax[i] > max) {
                    max = minimax[i];
                    idx = i;
                }
            }

            int move = getPossibleMoves(board).get(idx);

            movePrint(move, board);
            if(checkGameOver(board) != 0) {
                //Collections.sort(maxNumPairs);
                System.out.println(maxNumPairs.toString());
                System.out.println("Game over yeet");
                return;
            }

            System.out.println("Num Pairs: " + pairs.length);
            maxNumPairs.add(pairs.length);

            int userMove = registerRandomMove(getPossibleMoves(board));
            movePrint(userMove, board);
        }

    }

    private static short[] findAllPairs(byte[] board) {
        ArrayList<pair> pairs = new ArrayList<>();
        List<Integer> nextMoves = getPossibleMoves(board);
        //System.out.println("next moves: " + nextMoves);
        for (int i = 0; i < nextMoves.size(); i++) {
            byte[] copy = copyOf(board, 93);
            move(nextMoves.get(i), copy);
            List<Integer> doubleMoves = getPossibleMoves(copy);
            for (int j = 0; j < doubleMoves.size(); j++) {
                pairs.add(new pair(nextMoves.get(i), doubleMoves.get(j)));
            }
        }
        short[] formattedPairs = new short[pairs.size()];
        int index = 0;
        for(pair p : pairs) {
            formattedPairs[index] = (short)(p.x * 100 + p.y);
            index++;
        }
        return formattedPairs;
    }

    private static class pair {
        int x;
        int y;

        private pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }

    private static void kernel(int idx, byte[] board, short[] pairs, short[] wins) {
        final int numTrials = 5000;
        short p = pairs[idx];
        int first = p/100;
        int second = p%100;
        byte[] copy = copyOf(board, 93);
        move(first, copy);
        move(second, copy);
        short w = 0;
        for(int t = 0; t < numTrials; t++) {
            byte[] cBoard = copyOf(copy, 93);
            int simulate = simulate(cBoard);
            if(simulate == 2) {
                w++;
            }
            if(t == 100 && w == 0) {
                wins[idx] = 0;
                return;
            }
            if(t == 500 && w == 500) {
                wins[idx] = 5000;
                return;
            }
        }
        wins[idx] = w;
    }

    private static int registerUserMove(List<Integer> possible) {
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
        return possible.get(random.nextInt(possible.size()));
    }
}
