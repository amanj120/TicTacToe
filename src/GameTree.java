import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTree {
    int move;
    int wins;
    int sims;
    ArrayList<GameTree> nextMoves = new ArrayList<>();

    public GameTree(){nextMoves = new ArrayList<>();}

    public GameTree(int move) {
        this.move = move;
        this.wins = 0;
        this.sims = 0;
    }

    public GameTree(int move, int wins, int sims) {
        this.move = move;
        this.wins = wins;
        this.sims = sims;
    }

    public GameTree(int move, byte[] board, int level) {
        this(move, board, level, 1);
    }

    public GameTree(int move, byte[] board, int level, int l) {
        if (level == l) {
            this.move = move;
        } else {
            this.move = move;
            byte[] copy = Arrays.copyOf(board, 94);
            try {
                Game.move(move, copy);
            } catch (Exception e){}
            if(!Game.isGameOver(copy)) {
                List<Integer> next = Game.getPossibleMoves(copy);
                for (Integer i : next) {
                    nextMoves.add(new GameTree(i, copy, level, l + 1));
                }
            }
        }
    }

    public boolean nextMovesContains(int i) {
        for(GameTree gt : nextMoves) {
            if(gt.move == i)
                return true;
        }
        return false;
    }


    public int getSize() {
        if(this.nextMoves.size() == 0) {
            return 1;
        } else {
            return nextMoves.stream().mapToInt(GameTree::getSize).sum();
        }
    }

    @Override
    public String toString() {
        return formatInt(move) + ":\t[" + wins + "/" + sims + "]";
    }

    public String formatInt(int i ) {
        if(i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

}
