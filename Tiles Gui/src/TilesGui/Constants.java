
package TilesGui;

/*
This class contains all constants used in the program.
*/

public final class Constants {

    private Constants() {}
    
    public static enum direction {UP, RIGHT, DOWN, LEFT};
    public static final int BOARD_SIZE = 9;
    public static final int MAX_MOVES = 4;
    public static final int[] ROWS = {0, 0, 0, 1, 1, 1, 2, 2, 2};
    public static final int[] COLS = {0, 1, 2, 0, 1, 2, 0, 1, 2};
}
