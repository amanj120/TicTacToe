import java.util.Arrays;

public class StatefulGame extends Game{



//    private static void playStatefulGame() {
//        BoardIO bio = new BoardIO();
//        byte[] board = new byte[93];
//        int startingMove = 99;
//        GameTree tree = new GameTree(startingMove);
//        for(int i : Game.getPossibleMoves(board)) {
//            tree.nextMoves.add(new GameTree(i));
//        }
//        System.out.println(BoardIO.asString(tree));
//
//        while(true) {
//            bio.printBoard(board);
//            int userMove = registerUserMove(getPossibleMoves(board));
//            movePrint(userMove, board);
//            for(GameTree gt: tree.nextMoves) {
//                if (gt.move == userMove) {
//                    tree = gt;
//                    break;
//                }
//            }
//            tree.move = userMove;
//            for(int i : getPossibleMoves(board)) {
//                if(!tree.nextMovesContains(i))
//                    tree.nextMoves.add(new GameTree(i));
//            }
//            //System.out.println(BoardIO.asString(tree));
//            int r = registerStatefulCPUMove(tree, board, 2);
//        }
//
//    }

//    private static int registerStatefulCPUMove(GameTree tree, byte[] board, int winNum) {
//        for(GameTree gt : tree.nextMoves) {
//            byte[] copy = Arrays.copyOf(board, 93);
//            move(gt.move, copy);
//            for(int i : getPossibleMoves(copy)) {
//                if(!tree.nextMovesContains(i)) {
//                    byte[] copy2 = Arrays.copyOf(copy, 93);
//                    move(i, copy2);
//                    int n = runSims(copy2, 2000, winNum);
//                    gt.nextMoves.add(new GameTree(i, n, 2000));
//                }
//            }
//        }
//        System.out.println(BoardIO.asString(tree));
//        return 0;
//    }
}
