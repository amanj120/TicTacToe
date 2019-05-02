public class Game {

    public static short[] board = new short[91];
    //indeces 1-81 are the individual 81 pieces of the board
    //indeces 82-90 store the 9 3x3 boards
    //index 91 stores the winner of the game (if any)
    //index 0 says the index of the last move played

    public static void checkWin(int boardNum) {
        int start = boardNum * 9;
        short[][] mini = new short[3][3];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                mini[i][j] = board[start + i*3 + j];
            }
        }
        short winner = 0;
        //rows
        for(int i = 0; i < 3; i++) {
            if(mini[i][0] != 0) {
                if(mini[i][0] == mini[i][1] && mini[i][1] == mini[i][2]) {
                    winner = mini[i][0];
                }
            }
        }
        //columns
        for(int i = 0; i < 3; i++) {
            if(mini[0][i] != 0) {
                if(mini[0][i] == mini[0][i] && mini[1][i] == mini[2][i]) {
                    winner = mini[i][0];
                }
            }
        }
        //diagonals
        if(mini[0][0] != 0 && mini[0][0] == mini[1][1] && mini[1][1] == mini[2][2]) {
            winner = mini[0][0];
        }
        if(mini[2][0] != 0 && mini[2][0] == mini[1][1] && mini[1][1] == mini[0][2]) {
            winner = mini[2][0];
        }

        if(winner != 0) {
            board[82 + boardNum] = winner;
        }
    }



}
