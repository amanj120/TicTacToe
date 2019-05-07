import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
public class Tests {

    @Test
    public void testCheckWin() {
        Game.board[10] = 3;
        Game.board[13] = 3;
        Game.board[16] = 3;
        //System.out.println(Arrays.toString(Game.board));
        Game.checkWin(1);
        //System.out.println(Arrays.toString(Game.board));
        assertEquals(Game.board[83], 3);
    }


}
