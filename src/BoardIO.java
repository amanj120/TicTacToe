import java.util.ArrayList;
import java.util.List;

class BoardIO {
    private static final String format = "" +
            "A0 A1 A2 | B0 B1 B2 | C0 C1 C2\n" +
            "A3 A4 A5 | B3 B4 B5 | C3 C4 C5\n" +
            "A6 A7 A8 | B6 B7 B8 | C6 C7 C8\n" +
            "---------+----------+---------\n" +
            "D0 D1 D2 | E0 E1 E2 | F0 F1 F2\t\t\t* * *\n" +
            "D3 D4 D5 | E3 E4 E5 | F3 F4 F5\t\t\t* * *\n" +
            "D6 D7 D8 | E6 E7 E8 | F6 F7 F8\t\t\t* * *\n" +
            "---------+----------+---------\n" +
            "G0 G1 G2 | H0 H1 H2 | I0 I1 I2\n" +
            "G3 G4 G5 | H3 H4 H5 | I3 I4 I5\n" +
            "G6 G7 G8 | H6 H7 H8 | I6 I7 I8\n";

    private static final int[] positions = {157, 159, 161, 196, 198, 200, 235, 237, 239};

    public static void printBoard(byte[] board) {
        System.out.println(asString(board));
    }

    public static void printPossibleMoves(byte[] board) {
        List<String> print = new ArrayList<>();
        List<Integer> moves = Game.getPossibleMoves(board);
        for(Integer i : moves) {
            print.add(getSeq(i));
        }
        System.out.println(print);
        //System.out.println(moves);
    }

    private static String asString(byte[] board) {
        String p = String.copyValueOf(format.toCharArray());
        for(int i = 0; i < 81; i++) {
            if(board[i] != 0) {
                String seq = getSeq(i);
                p = p.replace(seq, (board[i] == 1? "><" : "<>"));
            }
        }
        char[] f = p.toCharArray();
        for(int i = 81; i < 90; i++)
            if(board[i] != 0)
                f[positions[i - 81]] = (board[i] == 1? 'X' : 'O');

        return String.valueOf(f);
    }

    static String getSeq(int i){
        return (char)(((i)/9) + 65) + "" + ((i)%9);
    }

    static int getInt(String seq){
        int a = (int) seq.toUpperCase().charAt(0);
        int b = Integer.parseInt(seq.charAt(1) + "");
        return ((a - 65) * 9) + b;
    }

    static void printGameTree(GameTree gameTree) {
        System.out.println(asString(gameTree));
    }

    static String t4 = "|\t |\t |\t |---";
    static String t3 = "|\t |\t |---";
    static String t2 = "|\t |---";
    static String t1 = "|---";
    static String t0 = "";

    static String asString(GameTree gameTree) {
        if(gameTree.level == 0) {
            return t4 + gameTree.move + "\n";
        } else if (gameTree.level == 1) {
            String ret = t3 + gameTree.move + "\n";
            for(GameTree gt : gameTree.nextMoves) {
                ret += asString(gt);
            }
            return ret;
        } else if (gameTree.level == 2) {
            String ret = t2 + gameTree.move + "\n";
            for(GameTree gt : gameTree.nextMoves) {
                ret += asString(gt);
            }
            return ret;
        } else if (gameTree.level == 3){
            String ret = t1 + gameTree.move + "\n";
            for(GameTree gt : gameTree.nextMoves) {
                ret += asString(gt);
            }
            return ret;
        } else {
            String ret = t0 + gameTree.move + "\n";
            for(GameTree gt : gameTree.nextMoves) {
                ret += asString(gt);
            }
            return ret;
        }
    }

//    public static void main(String[] args) {
//        System.out.print("int[] positions = {");
//        for(char start = 'a'; start <= 'i'; start++) {
//            System.out.print(format.indexOf(start) + ", ");
//        }
//        System.out.println("};");
//    }
}
