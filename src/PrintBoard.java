public class PrintBoard {
    private static final String format = "" +
            "A0 A1 A2 | B0 B1 B2 | C0 C1 C2\n" +
            "A3 A4 A5 | B3 B4 B5 | C3 C4 C5\n" +
            "A6 A7 A8 | B6 B7 B8 | C6 C7 C8\n" +
            "------------------------------\n" +
            "D0 D1 D2 | E0 E1 E2 | F0 F1 F2\n" +
            "D3 D4 D5 | E3 E4 E5 | F3 F4 F5\n" +
            "D6 D7 D8 | E6 E7 E8 | F6 F7 F8\n" +
            "------------------------------\n" +
            "G0 G1 G2 | H0 H1 H2 | I0 I1 I2\n" +
            "G3 G4 G5 | H3 H4 H5 | I3 I4 I5\n" +
            "G6 G7 G8 | H6 H7 H8 | I6 I7 I8\n";

    public static void print(int[] board) {
        String p = String.copyValueOf(format.toCharArray());
        for(int i = 1; i <= 81; i++) {
            if(board[i] != 0) {
                String seq = (char)((i/9) + 65) + "" + ((i-1)%9);
                System.out.println(seq);
                p = p.replace(seq, (board[i] == 1? "><" : "[]"));
            }
        }
        System.out.println(p);
    }
}
