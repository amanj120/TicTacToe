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

    private static final int GAME_OVER = 90;
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

    public static boolean isGameOver(byte[] board) {
        return (board[GAME_OVER] != 0 || getPossibleMoves(board).size() == 0);
    }

    static void move(int index, byte[] board) {
        if(board[index] == 0) {
            board[index] = (byte)((board[NUM_MOVES] % 2) + 1);
            board[NUM_MOVES]++;
            board[LAST_MOVE] = (byte) index;
            int before = board[parent(index)];
            board[parent(index)] = check3x3board((index/9), board);
            if(before != board[parent(index)]) {
                board[GAME_OVER] = check3x3board(9, board);
            }
        } else {
            throw new IllegalArgumentException("not a legal move: that space is taken");
        }
    }

    static List<Integer> getPossibleMoves(byte[] board) {
        List<Integer> ret = new ArrayList<>();
        byte last = board[LAST_MOVE];
        if ((board[parent(send(last))] != 0) || (board[NUM_MOVES] == 0)) {
            for (int i = 0; i < 81; i++) {
                if(board[i] == 0 && board[parent(i)] == 0) {
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
        if(board[GAME_OVER] != 0) {
            return board[GAME_OVER];
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
        playStatefulGame();
    }

    private static void playStatefulGame() {
        byte[] board = new byte[93];
        int startingMove = 99;
        GameTree tree = new GameTree(startingMove);
        for(int i : getPossibleMoves(board)) {
            tree.nextMoves.add(new GameTree(i));
        }
        System.out.println(BoardIO.asString(tree));

        while(true) {
            BoardIO.printBoard(board);
            int userMove = registerUserMove(getPossibleMoves(board));
            movePrint(userMove, board);
            for(GameTree gt: tree.nextMoves) {
                if (gt.move == userMove) {
                    tree = gt;
                    break;
                }
            }
            tree.move = userMove;
            for(int i : getPossibleMoves(board)) {
                if(!tree.nextMovesContains(i))
                    tree.nextMoves.add(new GameTree(i));
            }
            //System.out.println(BoardIO.asString(tree));
            int r = registerStatefulCPUMove(tree, board, 2);
        }

    }

    private static int registerStatefulCPUMove(GameTree tree, byte[] board, int winNum) {
        for(GameTree gt : tree.nextMoves) {
            byte[] copy = Arrays.copyOf(board, 93);
            move(gt.move, copy);
            for(int i : getPossibleMoves(copy)) {
                if(!tree.nextMovesContains(i)) {
                    byte[] copy2 = Arrays.copyOf(copy, 93);
                    move(i, copy2);
                    int n = runSims(copy2, 2000, winNum);
                    gt.nextMoves.add(new GameTree(i, n, 2000));
                }
            }
        }
        System.out.println(BoardIO.asString(tree));
        return 0;
    }

    private static void playStatelessGame() {
        byte[] board = new byte[93];
        GameTree poss;// = new GameTree(40, board, 2, 0);
        movePrint(40, board);

        ArrayList<Integer> nums = new ArrayList<>();

        //BoardIO.printGameTree(poss);

        while(true) {
            int userMove = registerUserMove(getPossibleMoves(board));
            //int userMove = registerCPUMove(poss, board, 2);
            poss = new GameTree(userMove, board, 2);
            movePrint(userMove, board);
            nums.add(poss.getSize());

            if(isGameOver(board)) {
                break;
            }

            //System.out.println("here");
            int cpuMove = registerCPUMove(poss, board, 1);
            //BoardIO.printGameTree(poss);
            //System.out.println(cpuMove);
            //BoardIO.printGameTree(poss);
            poss = new GameTree(cpuMove, board, 2);
            movePrint(cpuMove, board);
            nums.add(poss.getSize());

            if(isGameOver(board)) {
                break;
            }
        }

        System.out.println(nums.toString());
        System.out.println("Game over yeet");
    }

    private static int registerCPUMove(GameTree tree, byte[] board, int winNum) {
        int nt = 2000;
        for(GameTree level1 : tree.nextMoves) {
            int min = nt;
            for(GameTree level2 : level1.nextMoves) {
                int first = level1.move;
                int second = level2.move;
                byte[] copy = Arrays.copyOf(board, 93);
                move(first, copy);
                move(second, copy);
                level2.wins = runSims(copy, nt, winNum);
                level2.sims = nt;
                //System.out.println(first + ", " + second + ": " + level2.wins);
                min = Math.min(min, level2.wins);
            }
            level1.wins = min;
            //System.out.println("\t\t" + level1.move + ", " + min);
            if (min == nt) {
                return level1.move;
            }
        }
        int max = -1;
        int ret = -1;
        for(GameTree level1 : tree.nextMoves) {
            //System.out.println(BoardIO.getSeq(level1.move) + ": " + level1.wins);
            if(level1.wins > max) {
                max = level1.wins;
                ret = level1.move;
            }
        }
        return ret;
    }

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
