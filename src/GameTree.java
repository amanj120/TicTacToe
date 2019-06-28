import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTree {
    int move;
    int level;
    int wins;
    int sims;
    ArrayList<GameTree> nextMoves = new ArrayList<>();

    public GameTree(int move, byte[] board, int level) {
        if (level == 0) {
            this.move = move;
            this.level = 0;
        } else {
            this.move = move;
            this.level = level;
            byte[] copy = Arrays.copyOf(board, 93);
            Game.move(move, copy);
            if(Game.isGameOver(copy)) {
                List<Integer> next = Game.getPossibleMoves(copy);
                for (Integer i : next) {
                    nextMoves.add(new GameTree(i, copy, level - 1));
                }
            }
        }
    }

    public int getSize() {
        if(level == 0) {
            return 1;
        } else {
            return nextMoves.stream().mapToInt(GameTree::getSize).sum();
        }
    }
}
