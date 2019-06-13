//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Scanner;
//
//class TerminalEngine extends Engine {
//
//    private static Scanner sc;
//    static String[] testCase = {
//            "A2", "C8", "I4", "E1",
//            "B0", "A7", "H0", "A0",
//            "A4", "E2", "C1", "B1",
//            "B8", "I2", "C7", "H2",
//            "C4", "A1", "B4", "D2"};
//    public static void main(String[] args) {
//
//        play();
//
//    }
//
//    public static void play() {
//        sc = new Scanner(System.in);
//        Game.init();
//        int idx = 0;
//        while(true) {
//            BoardIO.printBoard();
//            BoardIO.printPossibleMoves();
//            registerUserMove(BoardIO.getInt(testCase[idx]));
//            idx++;
//            BoardIO.printBoard();
//            if(checkGameOver()) {
//                System.out.println("Winner " + getWinner());
//            }
//            requestCPUMove();
//            registerCPUMove(BoardIO.getInt(testCase[idx]));
//            idx++;
//        }
//        //System.out.println("Winner " + getWinner());
//    }
//    /*
//    game flow:
//
//    ask player to choose X or O
//    ask player to choose playing first or second
//
//    while(game not over) {
//        ask player 1 for a move
//        check win
//        ask player 2 for a move
//        check win
//    }
//    */
//
//    public static int requestUserMove() {
//        System.out.println("Please select a move");
//        String input = sc.next();
//        List<Integer> possibleMoves = Game.getPossibleMoves();
//        while(true) {
//            try {
//                int x = BoardIO.getInt(input);
//                if(possibleMoves.contains(x)) {
//                    return x;
//                }
//                System.out.println("Please select a move");
//                input = sc.next();
//            } catch (Exception e) {
//                System.out.println("That was an invalid move, try again");
//                input = sc.next();
//            }
//        }
//    }
//
//    public static int requestCPUMove() {
//        List<Integer> possibleMoves = Game.getPossibleMoves();
//        System.out.println("possible moves for the CPU:");
//        BoardIO.printPossibleMoves();
//        int[] probs = new int[possibleMoves.size()];
//        for(int i = 0; i < probs.length; i++) {
//            for(int t = 0; t < 2; t++) {
//                if (Game.simulate(possibleMoves.get(i)) == 2) {
//                    probs[i]++;
//                }
//            }
//        }
//        //System.out.println(Arrays.toString(probs));
//        int idx = 0;
//        for (int i = 0; i < probs.length; i++) {
//            if(probs[i] > probs[idx]) {
//                idx = i;
//            }
//        }
//        System.out.println(Arrays.toString(probs));
//        System.out.println("CPU Chooses " + BoardIO.getSeq(possibleMoves.get(idx)));
//        Scanner s = new Scanner(System.in);
//        String l = s.next();
//        return BoardIO.getInt(l);
//        //return possibleMoves.get((int)(Math.random() * (double)possibleMoves.size()));
//    }
//}
