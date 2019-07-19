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
    private static final int PLAYER_MOVE_NUMBER = 93; // is the CPU the first or second player to move
    private static final int BOARD_LENGTH = 94;
    private static BoardIO bio;
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

    private static int simulate(byte[] board) {
        if(board[GAME_OVER] != 0) {
            return board[GAME_OVER];
        } else {
            List<Integer> moves = getPossibleMoves(board);
            if(moves.size() == 0) { //tied game
                return -1;
            }
            int nextIdx = getRandomInt(moves.size());
            move(moves.get(nextIdx), board);
            return simulate(board);
        }
    }

    static boolean playerMovesFirst(byte[] board) {
        return board[PLAYER_MOVE_NUMBER] == 1;
    }

    public static void registerPlayerMoveNumber(byte[] board, byte playerMoveNumber) {
        board[PLAYER_MOVE_NUMBER] = playerMoveNumber;
    }

    static int registerCPUMove(byte[] board) {
        int level = 1;
        int winNum = 3 - board[PLAYER_MOVE_NUMBER];
        GameTree tree = new GameTree(board[LAST_MOVE], board, level);

        int numLeaves = 1000;
        //find a level with about 4000 moves: about 1000 sims per leaf node

        int minLeaves = numLeaves;
        int actualLeaves = 0;
        int curLev = 0;
        while(tree.getSize() < numLeaves) {
            tree.addLevel(board);
            curLev++;
            //System.out.println(tree.getSize());
            //System.out.println(minDifferenceBetweenExpectedTreeSize);
            if (Math.abs(tree.getSize() - numLeaves) < minLeaves){
                level = curLev;
                minLeaves = Math.abs((tree.getSize() - numLeaves));
                actualLeaves = tree.getSize();
            }
        }
        System.out.println(actualLeaves);
        System.out.println(level);

        int nt = (int) (1000000L / (long) actualLeaves);
        System.out.println(nt);
        minimax(tree, level, nt, board);

        for(GameTree level1 : tree.nextMoves) {
            int min = nt;
            for(GameTree level2 : level1.nextMoves) {
                int first = level1.move;
                int second = level2.move;
                byte[] copy = Arrays.copyOf(board, BOARD_LENGTH);
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
        //BoardIO.printGameTree(tree);
        return ret;
    }

    public static int minimax(GameTree tree, int levels, int numTrials, byte[] board) {
        //BoardIO.printGameTree(tree);
        for(GameTree gt : tree.nextMoves) {
            minimax(gt, levels - 1, true, numTrials, board);
        }
        BoardIO.printGameTree(tree);
        return 0;
    }

    public static void minimax(GameTree tree, int levelsLeft, boolean useMin, int numTrials, byte[] board){
        if (levelsLeft == 0) {
            tree.wins = runSims(board, numTrials, 3 - board[PLAYER_MOVE_NUMBER]);
        } else {
            for(GameTree gt : tree.nextMoves) {
                byte[] copy = Arrays.copyOf(board, BOARD_LENGTH);
                move(tree.move, copy);
                minimax(gt, levelsLeft - 1, !useMin, numTrials, copy);
            }
            if(useMin) {
                int min = numTrials;
                for(GameTree gt : tree.nextMoves) {
                    if(gt.wins < min) {
                        min = gt.wins;
                    }
                }
                tree.wins = min;
            } else {
                int max = -1;
                for(GameTree gt : tree.nextMoves) {
                    if(gt.wins > max) {
                        max = gt.wins;
                    }
                }
                tree.wins = max;
            }
        }
    }

    public static int runSims(byte[] board, int numTrials, int winNum) {
        int ret = 0;
        for(int i = 0; i < numTrials; i++) {
            byte[] copy = copyOf(board, BOARD_LENGTH);
            int simulate = simulate(copy);
            if(simulate == winNum) {
                ret++;
            }
        }
        return ret;
    }

    private static int getRandomInt(int max) {
        return (int) (Math.random() * max);
    }
}