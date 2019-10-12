/**
 * Creates a game of minesweeper and handles all game logic and information.
 * To start a new game use {@code init()}, a single instance may play multiple games this way.
 * {@code status} determines the status of the game, there is no other way to determine if a game is complete.
 */
public class GameBoard {

    /**
     * ROWS: Horizontal lines.
     * COLS: Vertical lines.
     * MINES: Number of mines.
     */
    public final int ROWS, COLS, MINES;
    /**
     * ComputerBoard: Store the mines and tile numbers.
     * PlayerBoard: Information about hidden and revealed tiles and flags:
     * (-1) flag, (0) hidden tile, (1) revealed tile.
     */
    private byte[][] computerBoard, playerBoard;
    /**
     * Status of the game: (-1) loss, (0) ongoing, (1) win.
     */
    private int status = 0;

    public GameBoard(int rows, int cols, int mines) {
        ROWS = rows;
        COLS = cols;
        MINES = mines;
    }

    /**
     * Clears each board and places mines.
     */
    public void init() {
        computerBoard = new byte[ROWS][COLS];
        int x, y;
        for (int i = 0; i < MINES; i++) {
            boolean set = false; // Prevents mines from overlapping
            do {
                x = (int) (ROWS * Math.random());
                y = (int) (COLS * Math.random());
                if (computerBoard[x][y] == 0) {
                    computerBoard[x][y] = -1;
                    set = true;
                }
            } while (!set);
        }
        // Determine count for tiles
        for (int m = 0; m < ROWS; m++) {
            for (int n = 0; n < COLS; n++) {
                if (computerBoard[m][n] == 0) {
                    computerBoard[m][n] = countAdjacent(-1, computerBoard, m, n);
                }
            }
        }
        playerBoard = new byte[ROWS][COLS];
        status = 0;
    }

    /**
     * Reveals a tile on the board.
     * @param x x coordinate.
     * @param y y coordinate.
     */
    public void reveal(int x, int y) {
        if (computerBoard[x][y] == -1) { // Reveal a mine
            status = -1;
        } else {
            if (playerBoard[x][y] == 1) {
                /*
                 * When a revealed tile is selected, and it has the same number of flags around it as its value,
                 * reveal all other tiles around this one.
                 */
                if (computerBoard[x][y] == countAdjacent(-1, playerBoard, x, y)) {
                    if (x > 0 && playerBoard[x - 1][y] == 0) {
                        if (computerBoard[x - 1][y] == -1) {
                            status = -1;
                            return;
                        }
                        playerBoard[x - 1][y] = 1;
                    }
                    if (x > 0 && y + 1 < COLS && playerBoard[x - 1][y + 1] == 0) {
                        if (computerBoard[x - 1][y + 1] == -1) {
                            status = -1;
                            return;
                        }
                        playerBoard[x - 1][y + 1] = 1;
                    }
                    if (y + 1 < COLS && playerBoard[x][y + 1] == 0) {
                        if (computerBoard[x][y + 1] == -1) {
                            status = -1;
                            return;
                        }
                        playerBoard[x][y + 1] = 1;
                    }
                    if (x + 1 < ROWS && y + 1 < COLS && playerBoard[x + 1][y + 1] == 0) {
                        if (computerBoard[x + 1][y + 1] == -1) {
                            status = -1;
                            return;
                        }
                        playerBoard[x + 1][y + 1] = 1;
                    }
                    if (x + 1 < ROWS && playerBoard[x + 1][y] == 0) {
                        if (computerBoard[x + 1][y] == -1) {
                            status = -1;
                            return;
                        }
                        playerBoard[x + 1][y] = 1;
                    }
                    if (x + 1 < ROWS && y > 0 && playerBoard[x + 1][y - 1] == 0) {
                        if (computerBoard[x + 1][y - 1] == -1) {
                            status = -1;
                            return;
                        }
                        playerBoard[x + 1][y - 1] = 1;
                    }
                    if (y > 0 && playerBoard[x][y - 1] == 0) {
                        if (computerBoard[x][y - 1] == -1) {
                            status = -1;
                            return;
                        }
                        playerBoard[x][y - 1] = 1;
                    }
                    if (x > 0 && y > 0 && playerBoard[x - 1][y - 1] == 0) {
                        if (computerBoard[x - 1][y - 1] == -1) {
                            status = -1;
                            return;
                        }
                        playerBoard[x - 1][y - 1] = 1;
                    }
                    checkBoard();
                }
            } else { // Reveal hidden tile
                playerBoard[x][y] = 1;
                checkBoard();
            }
        }
    }

    /**
     * Flags a tile on the board.
     * @param x x coordinate.
     * @param y y coordinate.
     */
    public void flag(int x, int y) {
        if (playerBoard[x][y] == 0) {
            playerBoard[x][y] = -1;
            checkFlags();
        } else if (playerBoard[x][y] == -1) {
            playerBoard[x][y] = 0;
        }
    }

    /**
     * Prints a representation on the board to the terminal.
     */
    public void print() {
        for (int n = 0; n < COLS; n++)
            System.out.print("\t\u001B[1m" + (n + 1) + "\u001B[39m");
        for (int m = 0; m < ROWS; m++) {
            System.out.print("\n\u001B[1m" + (m + 1) + "\u001B[39m");
            for (int n = 0; n < COLS; n++) {
                switch (playerBoard[m][n]) {
                    case -1:
                        System.out.print("\t\u001B[1m\u001B[31mF\u001B[39m");
                        break;
                    case 0:
                        System.out.print("\t?");
                        break;
                    case 1:
                        switch (computerBoard[m][n]) {
                            case 0:
                                System.out.print("\t\u001B[37m0\u001B[39m");
                                break;
                            case 1:
                                System.out.print("\t\u001B[1m\u001B[36m1\u001B[39m");
                                break;
                            case 2:
                                System.out.print("\t\u001B[1m\u001B[32m2\u001B[39m");
                                break;
                            case 3:
                                System.out.print("\t\u001B[1m\u001B[91m3\u001B[39m");
                                break;
                            case 4:
                                System.out.print("\t\u001B[1m\u001B[34m4\u001B[39m");
                                break;
                            case 5:
                                System.out.print("\t\u001B[1m\u001B[33m5\u001B[39m");
                                break;
                            case 6:
                                System.out.print("\t\u001B[1m\u001B[35m6\u001B[39m");
                                break;
                            case 7:
                                System.out.print("\t\u001B[1m\u001B[96m7\u001B[39m");
                                break;
                            case 8:
                                System.out.print("\t\u001B[1m\u001B[31m8\u001B[39m");
                                break;
                        }
                        break;
                }
            }
        }
        System.out.println();
    }

    /**
     * Prints the complete board, including bombs and mis-flags.
     */
    public void printComplete() {
        for (int n = 0; n < COLS; n++)
            System.out.print("\t\u001B[1m" + (n + 1) + "\u001B[39m");
        for (int m = 0; m < ROWS; m++) {
            System.out.print("\n\u001B[1m" + (m + 1) + "\u001B[39m");
            for (int n = 0; n < COLS; n++) {
                if (playerBoard[m][n] == -1) {
                    System.out.print(computerBoard[m][n] == -1 ?
                            "\t\u001B[1m\u001B[31mF\u001B[39m" :
                            "\t\u001B[1m\u001B[33mF\u001B[39m");
                } else {
                    switch (computerBoard[m][n]) {
                        case -1:
                            System.out.print("\t\u001B[1m\u001B[31mX\u001B[39m");
                            break;
                        case 0:
                            System.out.print("\t\u001B[37m0\u001B[39m");
                            break;
                        case 1:
                            System.out.print("\t\u001B[1m\u001B[36m1\u001B[39m");
                            break;
                        case 2:
                            System.out.print("\t\u001B[1m\u001B[32m2\u001B[39m");
                            break;
                        case 3:
                            System.out.print("\t\u001B[1m\u001B[91m3\u001B[39m");
                            break;
                        case 4:
                            System.out.print("\t\u001B[1m\u001B[34m4\u001B[39m");
                            break;
                        case 5:
                            System.out.print("\t\u001B[1m\u001B[33m5\u001B[39m");
                            break;
                        case 6:
                            System.out.print("\t\u001B[1m\u001B[35m6\u001B[39m");
                            break;
                        case 7:
                            System.out.print("\t\u001B[1m\u001B[96m7\u001B[39m");
                            break;
                        case 8:
                            System.out.print("\t\u001B[1m\u001B[31m8\u001B[39m");
                            break;
                    }
                }
            }
        }
        System.out.println();
    }

    /**
     * Returns the current status of the game.
     * @return status: (-1) loss, (0) ongoing, (1) win.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Counts the number of values appearing around a certain coordinate.
     * @param value value to be searched for.
     * @param board board searched.
     * @param x x coordinate.
     * @param y y coordinate.
     * @return the count of values found.
     */
    private byte countAdjacent(int value, byte[][] board, int x, int y) {
        byte count = 0;
        if (x > 0 && board[x - 1][y] == value) count++;
        if (x > 0 && y + 1 < COLS && board[x - 1][y + 1] == value) count++;
        if (y + 1 < COLS && board[x][y + 1] == value) count++;
        if (x + 1 < ROWS && y + 1 < COLS && board[x + 1][y + 1] == value) count++;
        if (x + 1 < ROWS && board[x + 1][y] == -1) count++;
        if (x + 1 < ROWS && y > 0 && board[x + 1][y - 1] == value) count++;
        if (y > 0 && board[x][y - 1] == value) count++;
        if (x > 0 && y > 0 && board[x - 1][y - 1] == value) count++;
        return count;
    }

    /**
     * Checks the board for revealed zeros and reveals the tiles around them.
     */
    private void checkBoard() {
        boolean found = false;
        for (int m = 0; m < ROWS; m++) {
            for (int n = 0; n < COLS; n++) {
                if (playerBoard[m][n] == 0 && computerBoard[m][n] >= 0 && (
                        m > 0 && playerBoard[m - 1][n] > 0 && computerBoard[m - 1][n] == 0 ||
                                m > 0 && n + 1 < COLS && playerBoard[m - 1][n + 1] > 0 && computerBoard[m - 1][n + 1] == 0 ||
                                n + 1 < COLS && playerBoard[m][n + 1] > 0 && computerBoard[m][n + 1] == 0 ||
                                m + 1 < ROWS && n + 1 < COLS && playerBoard[m + 1][n + 1] > 0 && computerBoard[m + 1][n + 1] == 0 ||
                                m + 1 < ROWS && playerBoard[m + 1][n] > 0 && computerBoard[m + 1][n] == 0 ||
                                m + 1 < ROWS && n > 0 && playerBoard[m + 1][n - 1] > 0 && computerBoard[m + 1][n - 1] == 0 ||
                                n > 0 && playerBoard[m][n - 1] > 0 && computerBoard[m][n - 1] == 0 ||
                                m > 0 && n > 0 && playerBoard[m - 1][n - 1] > 0 && computerBoard[m - 1][n - 1] == 0)) {
                    playerBoard[m][n] = 1;
                    found = true;
                }
            }
        }
        if (found) checkBoard();
    }

    /**
     * Determines if all the mines have been flagged correctly.
     */
    private void checkFlags() {
        int flags = 0;
        for (int m = 0; m < ROWS; m++) {
            for (int n = 0; n < COLS; n++) {
                if (playerBoard[m][n] < 0) {
                    if (computerBoard[m][n] < 0) {
                        flags++;
                    } else {
                        return; // Mis-flag
                    }
                }
            }
        }
        if (flags == MINES) status = 1;
    }

}
