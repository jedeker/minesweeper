/**
 * Contains the difficulty presets for the game.
 */
public enum  Difficulty {

    EASY(8, 8, 8),
    NORMAL(12, 12, 20),
    HARD(16, 16, 40),
    INSANE(16, 24, 60),
    EXPERT(16, 32, 100);

    public final int ROWS, COLS, MINES;

    Difficulty(int rows, int cols, int mines) {
        ROWS = rows;
        COLS = cols;
        MINES = mines;
    }
}

