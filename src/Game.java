import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private static ArrayList<Integer> maxNumPairs = new ArrayList<>();

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

    public static byte checkGameOver(byte[] board) {
        return check3x3board(9, board);
    }

    public static boolean isGameOver(byte[] board) {
        return (checkGameOver(board) != 0 || getPossibleMoves(board).size() == 0);
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
            if(ret.size() != 0) {
                return ret;
            } else {
                for (int i = 0; i < 81; i++) {
                    if(board[i] == 0 && board[parent((byte)i)] == 0){
                        ret.add(i);
                    }
                }
                return ret;
            }
        }
    }

    private static int parent(int n) {
        return ((n/9) + 81);
    }

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
        movePrint(40, board);

        ArrayList<Integer> nums = new ArrayList<>();
        GameTree poss;

        while(true) {
            int userMove = registerUserMove(getPossibleMoves(board));
            poss = new GameTree(userMove, board, 2);
            movePrint(userMove, board);
            nums.add(poss.getSize());

            if(isGameOver(board)) {
                break;
            }

            int cpuMove = registerCPUMove(poss, board);
            poss = new GameTree(cpuMove, board, 2);
            movePrint(cpuMove, board);
            nums.add(poss.getSize());
        }

        System.out.println(nums.toString());
        System.out.println("Game over yeet");
    }

    private static int registerCPUMove(GameTree tree, byte[] board) {
        for(GameTree level1 : tree.nextMoves) {
            int min = 5000;
            ArrayList<Integer> w = new ArrayList<>();
            for(GameTree level2 : level1.nextMoves) {
                int first = level1.move;
                int second = level2.move;
                byte[] copy = Arrays.copyOf(board, 93);
                level2.wins = runSims(copy, 5000, 1);
                level2.sims = 5000;
                //System.out.println(first + ", " + second + ": " + level2.wins);
                min = Math.min(min, level2.wins);
                w.add(level2.wins);
            }
            Collections.sort(w);

            if(w.size() > 0 && w.get(0) == 0) {
                level1.wins = 0;
            } else {
                int ww;
                if (w.size() > 3) {
                    ww = (w.get(0) + w.get(1) + w.get(2)) / 3;
                } else if (w.size() == 2) {
                    ww = (w.get(0) + w.get(1)) / 2;
                } else if (w.size() == 1) {
                    ww = w.get(0);
                } else {
                    ww = 0;
                }
                level1.wins = ww;
            }

        }
        int max = -1;
        int ret = -1;
        for(GameTree level1 : tree.nextMoves) {
            System.out.println(BoardIO.getSeq(level1.move) + ": " + level1.wins);
            if(level1.wins > max) {
                max = level1.wins;
                ret = level1.move;
            }
        }
        return ret;
    }

/*
    private static int registerCPUMove(byte[] board) {
        short[] pairs = findAllPairs(board);
        maxNumPairs.add(pairs.length);
        System.out.println("Num Pairs: " + pairs.length);
        int move = findMiniMaxIdx(board, pairs);
        return move;
    }

    private static int findMiniMaxIdx(byte[] board, short[] pairs) {
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
        int idx =-1;
        for (int i = 0; i < minimax.length; i++) {
            if (minimax[i] >= max) {
                max = minimax[i];
                idx = i;
            }
        }

        int move = getPossibleMoves(board).get(idx);
        return move;
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
*/

    public static int runSims(byte[] board, int numTrials, int winNum) {
        int ret = 0;
        for(int i = 0; i < numTrials; i++) {
            byte[] copy = copyOf(board, 93);
            int simulate = simulate(copy);
            if(simulate == winNum) {
                ret++;
            }
            if(i == numTrials / 10) {
                if (ret == 0) {
                    return 0;
                }
                if (ret == numTrials / 10) {
                    return numTrials;
                }
            }
        }
        return ret;
    }

    private static void kernel(int idx, byte[] board, short[] pairs, short[] wins) {
        int numTrials = 5000;
        short p = pairs[idx];
        int first = p/100;
        int second = p%100;
        byte[] copy = copyOf(board, 93);
        move(first, copy);
        move(second, copy);
        wins[idx] = (short) runSims(copy, numTrials, 1);
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
