import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter [ROW] [COL] to reveal a position (Rows run horizontally: Columns run vertically).\n" +
                "Enter f to toggle flagging.");
        // Prompt Difficulty
        Difficulty difficulty = null;
        do {
            System.out.println("Please select a difficulty:");
            for (Difficulty d : Difficulty.values())
                System.out.printf("\t(%d) %s : %dx%d : %d\n", d.ordinal() + 1, d.name(), d.COLS, d.ROWS, d.MINES);
            try {
                difficulty = Difficulty.values()[Integer.parseInt(scanner.next()) - 1];
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Not an option.");
            }
        } while (difficulty == null);
        // Create game
        GameBoard gameBoard = new GameBoard(difficulty.ROWS, difficulty.COLS, difficulty.MINES);
        gameBoard.init();
        // Begin game loop
        boolean flag = false; // Determines if a tile should be flagged
        gameBoard.print();
        do {
            String next = scanner.next();
            if (next.equalsIgnoreCase("F")) {
                flag = !flag;
                System.out.println(flag ? "Flagging on." : "Flagging off.");
            } else {
                int x, y;
                try {
                    x = Integer.parseInt(next) - 1;
                    y = Integer.parseInt(scanner.next()) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.\nEnter [ROW] [COL] to reveal a tile.\nEnter 'F' to flag.");
                    continue;
                }
                if (x < 0 || x >= gameBoard.ROWS || y < 0 || y >= gameBoard.COLS) {
                    System.out.println("Coordinate out of bounds.");
                    continue;
                }
                if (flag) {
                    gameBoard.flag(x, y);
                } else {
                    gameBoard.reveal(x, y);
                }
                gameBoard.print();
            }
        } while (gameBoard.getStatus() == 0);
        switch (gameBoard.getStatus()) {
            case -1:
                System.out.println("You Lose!");
                break;
            case 1:
                System.out.println("You Win!");
                break;
        }
        gameBoard.printComplete();
    }
}


