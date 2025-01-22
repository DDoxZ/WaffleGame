/**
 * WaffleGame
 * 
 * @author pedrosilva 59886
 * @author diogolopes 60447
 */
public class WaffleGame {

    private Puzzle puzzle;
    private final char[][] initialGrid;
    private char[][] grid;
    private int swapCount;

    /**
     * Checks if the given array only contains zeros
     * 
     * @param array the array
     * @return true if the array only contains zeros, false otherwise
     */
    private static boolean doesArrayOnlyContainZeros(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0)
                return false;
        }
        return true;
    }

    /**
     * Check if a letter in the given position is correct
     * 
     * @param puzzle the puzzle
     * @param grid   the grid
     * @param row    the row
     * @param col    the column
     * @requires {@code validPosition(row, col)}
     * @return true if the letter in the given position is correct, false otherwise
     */
    private static boolean isCorrectPosition(Puzzle puzzle, char[][] grid, int row, int col) {
        char c = grid[row - 1][col - 1];

        if (row % 2 == 1) {
            char c2 = puzzle.getLetterInHorizontalWord(col, (row + 1) / 2);

            if (c == c2)
                return true;
        } else if (col % 2 == 1) {
            char c2 = puzzle.getLetterInVerticalWord(row, (col + 1) / 2);

            if (c == c2)
                return true;
        }

        return false;
    }

    /**
     * Gets the number of occurrences in Puzzle of a given character in a given
     * column until a given row
     * 
     * @param puzzle the puzzle
     * @param row    the row
     * @param col    the column
     * @param c      the character
     * @requires {@code validPosition(row, col)}
     * @return the number of occurrences of the given character in the given column
     *         until the given row
     */
    private static int countOccurrencesInPuzzleColumnUntilRow(Puzzle puzzle, int row, int col, char c) {
        int count = 0;
        int wordIndex = (col + 1) / 2;

        for (int i = 0; i < row; i++) {
            if (puzzle.getLetterInVerticalWord(i + 1, wordIndex) == c)
                count++;
        }

        return count;
    }

    /**
     * Gets the number of occurrences in Grid of a given character in a given
     * column until a given row
     * 
     * @param grid the grid
     * @param row  the row
     * @param col  the column
     * @param c    the character
     * @requires {@code validPosition(row, col)}
     * @return the number of occurrences of the given character in the given column
     *         until the given row
     */
    private static int countOccurrencesInGridColumnUntilRow(char[][] grid, int row, int col, char c) {
        int count = 0;

        for (int i = 0; i < row; i++) {
            if (grid[i][col - 1] == c)
                count++;
        }

        return count;
    }

    /**
     * Gets the number of correct occurrences of a given character in a given row
     * until a given column
     * 
     * @param puzzle a valid grid
     * @param grid   a valid grid
     * @param col    a valid column
     * @param c      a valid character
     * @return the number of correct occurrences of the given character
     *         in the given
     *         row until the given column
     */
    private static int countCorrectOccurrencesInColumn(Puzzle puzzle, char[][] grid, int col, char c) {
        int count = 0;

        for (int i = 0; i < puzzle.size(); i++) {
            char charGrid = grid[i][col - 1];
            char charPuzzle = puzzle.getLetterInVerticalWord(i + 1, (col + 1) / 2);

            if (charGrid == charPuzzle && charPuzzle == c)
                count++;
        }

        return count;
    }

    /**
     * Verifies if a letter in a given row and column is in the correct column
     * according to some rules
     * 
     * @param puzzle the puzzle
     * @param grid   the grid
     * @param row    the row
     * @param col    the column
     * @requires {@code validPosition(row, col)}
     * @return true if the letter in the given position is in the correct column,
     *         false otherwise
     */
    private static boolean letterInColWrongPos(Puzzle puzzle, char[][] grid, int row, int col) {
        if (col % 2 == 0)
            return false;

        char c = grid[row - 1][col - 1];

        int occurrencesPuzzle = countOccurrencesInPuzzleColumnUntilRow(puzzle, puzzle.size(), col, c);
        int correctOccurrences = countCorrectOccurrencesInColumn(puzzle, grid, col,
                c);

        if (occurrencesPuzzle == 0)
            return false;
        if (correctOccurrences == occurrencesPuzzle)
            return false;

        int occurrencesGridUntilNow = countOccurrencesInGridColumnUntilRow(grid, row - 1,
                col, c);
        if (occurrencesPuzzle <= occurrencesGridUntilNow)
            return false;

        return true;
    }

    /**
     * Gets the number of occurrences in Puzzle of a given character in a given
     * column until a given row
     * 
     * @param puzzle the puzzle
     * @param row    the row
     * @param col    the column
     * @param c      the character
     * @requires {@code validPosition(row, col)}
     * @return the number of occurrences of the given character in the given column
     *         until the given row
     */
    private static int countOccurrencesInPuzzleRowUntilColumn(Puzzle puzzle, int row, int col, char c) {
        int count = 0;
        int wordIndex = (row + 1) / 2;

        for (int i = 0; i < col; i++) {
            if (puzzle.getLetterInHorizontalWord(i + 1, wordIndex) == c)
                count++;
        }

        return count;
    }

    /**
     * Gets the number of occurrences in Grid of a given character in a given
     * column until a given row
     * 
     * @param grid the grid
     * @param row  the row
     * @param col  the column
     * @param c    the character
     * @requires {@code validPosition(row, col)}
     * @return the number of occurrences of the given character in the given column
     *         until the given row
     */
    private static int countOccurrencesInGridRowUntilColumn(char[][] grid, int row, int col, char c) {
        int count = 0;

        for (int i = 0; i < col; i++) {
            if (grid[row - 1][i] == c)
                count++;
        }

        return count;
    }

    /**
     * Gets the number of correct occurrences of a given character in a given row
     * until a given column
     * 
     * @param puzzle a valid grid
     * @param grid   a valid grid
     * @param col    a valid column
     * @param c      a valid character
     * @return the number of correct occurrences of the given character
     *         in the given
     *         row until the given column
     */
    private static int countCorrectOccurrencesInRow(Puzzle puzzle, char[][] grid, int row, char c) {
        int count = 0;

        for (int i = 0; i < puzzle.size(); i++) {
            char charGrid = grid[row - 1][i];
            char charPuzzle = puzzle.getLetterInHorizontalWord(i + 1, (row + 1) / 2);

            if (charGrid == charPuzzle && charPuzzle == c)
                count++;
        }

        return count;
    }

    /**
     * Verifies if a letter in a given row and column is in the correct row
     * according to some rules
     * 
     * @param puzzle the puzzle
     * @param grid   the grid
     * @param row    the row
     * @param col    the column
     * @requires {@code validPosition(row, col)}
     * @return true if the letter in the given position is in the correct row,
     *         false otherwise
     */
    private static boolean letterInRowWrongPos(Puzzle puzzle, char[][] grid, int row, int col) {
        if (row % 2 == 0)
            return false;

        char c = grid[row - 1][col - 1];
        int occurrencesPuzzle = countOccurrencesInPuzzleRowUntilColumn(puzzle, row, puzzle.size(), c);
        int correctOccurrences = countCorrectOccurrencesInRow(puzzle, grid, row, c);

        if (occurrencesPuzzle == 0)
            return false;
        if (correctOccurrences == occurrencesPuzzle)
            return false;

        int occurrencesGridUntilNow = countOccurrencesInGridRowUntilColumn(grid, row, col - 1, c);
        if (occurrencesPuzzle <= occurrencesGridUntilNow)
            return false;

        return true;
    }

    /**
     * Verifies is the grid is valid for the puzzle given
     * 
     * @param puzzle the puzzle
     * @param grid   the grid
     * @return true if the grid is valid for the puzzle, false otherwise
     */
    public static boolean validGrid(Puzzle puzzle, char[][] grid) {
        if (puzzle == null)
            return false;
        if (grid == null)
            return false;

        int size = puzzle.size();

        if (grid.length != size)
            return false;

        for (int i = 0; i < size; i++) {
            if (grid[i] == null)
                return false;
            if (grid[i].length != size)
                return false;
        }

        int[] letters = new int[26];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char c = grid[i][j];

                if (i % 2 == 1 && j % 2 == 1) {
                    if (c != '*')
                        return false;
                } else {
                    if (!Character.isLetter(c))
                        return false;
                    if (!Character.isUpperCase(c))
                        return false;

                    letters[c - 'A']++;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i % 2 == 0) {
                    char c = puzzle.getLetterInHorizontalWord(j + 1, (i + 1) / 2 + 1);
                    letters[c - 'A']--;
                } else if (j % 2 == 0) {
                    char c = puzzle.getLetterInVerticalWord(i + 1, (j + 1) / 2 + 1);
                    letters[c - 'A']--;
                }
            }
        }

        if (!doesArrayOnlyContainZeros(letters))
            return false;

        return true;
    }

    /**
     * Constructs a WaffleGame with the given puzzle and grid
     * 
     * @param puzzle the puzzle
     * @param grid   the grid
     * @requires {@code validGrid(puzzle, grid)}
     */
    public WaffleGame(Puzzle puzzle, char[][] grid) {
        this.puzzle = puzzle;
        this.swapCount = 0;

        this.grid = new char[grid.length][grid.length];
        this.initialGrid = new char[grid.length][grid.length];
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                this.grid[i][j] = grid[i][j];
                this.initialGrid[i][j] = grid[i][j];
            }
        }
    }

    /**
     * Verifies if the given position is valid
     * 
     * @param row the row
     * @param col the column
     * @return true if the position is valid, false otherwise
     */
    public boolean validPosition(int row, int col) {
        if (row < 1 || row > this.puzzle.size())
            return false;
        if (col < 1 || col > this.puzzle.size())
            return false;
        if (row % 2 == 0 && col % 2 == 0)
            return false;

        return true;
    }

    /**
     * Returns the status of the letter in the given position
     * 
     * @param row the row
     * @param col the column
     * @requires {@code validPosition(row, col)}
     * @return the status of the letter in the given position
     */
    public LetterStatus clue(int row, int col) {

        if (isCorrectPosition(this.puzzle, this.grid, row, col)) {
            return LetterStatus.CORRECT_POS;
        } else if (letterInColWrongPos(this.puzzle, this.grid, row, col)) {
            return LetterStatus.WRONG_POS;
        } else if (letterInRowWrongPos(this.puzzle, this.grid, row, col)) {
            return LetterStatus.WRONG_POS;
        }

        return LetterStatus.INEXISTENT;
    }

    /**
     * Verifies if the characters position in a given row and column is allowed to
     * be swapped
     * 
     * @param row the row
     * @param col the column
     * @return true if the characters position in the given position is allowed to
     *         be swapped, false otherwise
     */
    public boolean swappablePosition(int row, int col) {
        if (!validPosition(row, col))
            return false;

        if (isCorrectPosition(this.puzzle, this.grid, row, col))
            return false;

        return true;
    }

    /**
     * Swaps the characters in the given positions
     * 
     * @param row1 the row of the first character
     * @param col1 the column of the first character
     * @param row2 the row of the second character
     * @param col2 the column of the second character
     * @requires {@code swappablePosition(row1, col1)}
     * @requires {@code swappablePosition(row2, col2)}
     * @requires {@code row1 != row2 || col1 != col2}
     */
    public void swap(int row1, int col1, int row2, int col2) {
        char c1 = this.grid[row1 - 1][col1 - 1];
        char c2 = this.grid[row2 - 1][col2 - 1];

        this.grid[row1 - 1][col1 - 1] = c2;
        this.grid[row2 - 1][col2 - 1] = c1;

        if (c1 != c2)
            this.swapCount++;
    }

    /**
     * Returns the maximum number of swaps that can be made in the grid
     * 
     * @return the maximum number of swaps that can be made in the grid
     */
    public int maxSwaps() {
        return this.puzzle.shuffleSwaps() + 5;
    }

    /**
     * Returns the number of remaining swaps
     * 
     * @return the number of remaining swaps
     */
    public int remainingSwaps() {
        return maxSwaps() - this.swapCount;
    }

    /**
     * Returns the current grid
     * 
     * @return the current grid
     */
    public char[][] getCurrentGrid() {
        return this.grid;
    }

    /**
     * Verifies if the puzzle was found
     * 
     * @return true if the puzzle was found, false otherwise
     */
    public boolean puzzleFound() {
        for (int i = 0; i < this.puzzle.size(); i++) {
            for (int j = 0; j < this.puzzle.size(); j++) {
                if (i % 2 == 0 || j % 2 == 0) {
                    if (!isCorrectPosition(this.puzzle, this.grid, i + 1, j + 1))
                        return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifies if the game is over
     * 
     * @return true if the game is over, false otherwise
     */
    public boolean isOver() {
        if (puzzleFound())
            return true;

        if (remainingSwaps() == 0)
            return true;

        return false;
    }

    /**
     * Restarts the game
     */
    public void restart() {
        this.grid = new char[this.grid.length][this.grid.length];

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[0].length; j++) {
                this.grid[i][j] = this.initialGrid[i][j];
            }
        }

        this.swapCount = 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String marginLeft = "      ";

        for (int i = 0; i < this.puzzle.size(); i++) {
            sb.append(marginLeft);

            for (int j = 0; j < this.puzzle.size(); j++) {
                String c = String.valueOf(grid[i][j]);
                String outputChar;

                if (c.equals("*")) {
                    outputChar = " ";
                } else if (clue(i + 1, j + 1) == LetterStatus.CORRECT_POS) {
                    outputChar = StringColouring.toGreenString(c);
                } else if (clue(i + 1, j + 1) == LetterStatus.WRONG_POS) {
                    outputChar = StringColouring.toYellowString(c);
                } else {
                    outputChar = c;
                }

                sb.append(outputChar + " ");
            }

            sb.append("\n");
        }

        sb.append("> " + remainingSwaps() + " swaps remaining <");

        return sb.toString();
    }
}
