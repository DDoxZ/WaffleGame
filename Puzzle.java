import java.util.Random;

/**
 * Puzzle
 * 
 * @author pedrosilva 59886
 * @author diogolopes 60447
 */
public class Puzzle {

    private final int size;
    private final String[] hWords;
    private final String[] vWords;

    /**
     * Returns a grid filled with the words in hWords and vWords
     * 
     * @param size   the length of each word
     * @param hWords a valid array of horizontal words
     * @param vWords a valid array of vertical words
     * @requires {@code validWords(size, hWords, vWords)}
     * @return a grid filled with the words in hWords and vWords
     */
    private static char[][] fillGrid(int size, String[] hWords, String[] vWords) {
        char[][] grid = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i % 2 != 0 && j % 2 != 0)
                    grid[i][j] = '*';

                if (i < (size + 1) / 2) {
                    grid[2 * i][j] = hWords[i].charAt(j);
                    grid[j][2 * i] = vWords[i].charAt(j);
                }
            }
        }

        return grid;
    }

    /**
     * Checks if the given coordinates belong to a diagonal
     * 
     * @param k    the row
     * @param l    the column
     * @param size the length of each word in the grid
     * @return true if the coordinates belong to a diagonal and false otherwise
     */
    private static boolean isDiagonal(int k, int l, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j && (i + j) != size - 1) {
                    if (i == k && j == l)
                        return false;
                }
            }
        }

        return true;
    }

    /**
     * Return a valid correct random position
     * 
     * @param size the length of each word in the grid
     * @ensures the coordinates do not belong to a diagonal; the position is
     *          contains a letter in the correct position
     * @return a valid correct random position
     */
    private static int[] getRandomCorrectPosition(int size, char[][] grid, String[] hWords, String[] vWords) {
        int i, j;
        boolean valid = false;

        Puzzle puzzle = new Puzzle(size, hWords, vWords);
        Random rand = new Random();
        WaffleGame game = new WaffleGame(puzzle, grid);

        do {
            i = rand.nextInt(size);
            j = rand.nextInt(size);

            if (i % 2 == 0 || j % 2 == 0) {
                if (i != j && (i + j) != (size - 1)) {
                    if (game.clue(j + 1, i + 1) == LetterStatus.CORRECT_POS) {
                        valid = true;
                    }
                } else {
                    i = rand.nextInt(size);
                    j = rand.nextInt(size);
                }
            }
        } while (!valid);

        return new int[] {
                i, j
        };
    }

    /**
     * Validates if all the words in hWords and vWords are valid acording some rules
     * 
     * @param size   an integer
     * @param hWords an array of strings
     * @param vWords an array of strings
     * @requires {@code hWords != null}
     * @requires {@code vWords != null}
     * @return true if all the words are valid and false otherwise
     */
    public static boolean validWords(int size, String[] hWords, String[] vWords) {
        if (size % 2 == 0 || size < 5 || size > 7)
            return false;

        if (hWords.length != (size + 1) / 2 || vWords.length != (size + 1) / 2)
            return false;

        for (int i = 0; i < hWords.length; i++) {
            if (hWords[i] == null || vWords[i] == null)
                return false;

            if (hWords[i].length() != size || vWords[i].length() != size)
                return false;

            for (int j = 0; j < hWords[i].length(); j++) {
                if (!Character.isUpperCase(hWords[i].charAt(j)) || !Character.isUpperCase(vWords[i].charAt(j)))
                    return false;
            }
        }

        return true;
    }

    /**
     * Validates if the words in hWords and vWords are overlapped
     * 
     * @param size   the length of each word
     * @param hWords a valid array of horizontal words
     * @param vWords a valid array of vertical words
     * @requires {@code validWords(size, hWords, vWords)}
     * @return true if the words are overlapped and false otherwise
     */
    public static boolean overlappedWords(int size, String[] hWords, String[] vWords) {
        for (int i = 0; i <= size / 2; i++) {
            for (int j = 0; j <= size / 2; j++) {
                if (hWords[i].charAt(2 * j) != vWords[j].charAt(2 * i))
                    return false;
            }
        }

        return true;
    }

    /**
     * Creates a new puzzle with the given size and words.
     * 
     * @param size   the length of each word
     * @param hWords a valid array of horizontal words
     * @param vWords a valid array of vertical words
     */
    public Puzzle(int size, String[] hWords, String[] vWords) {
        this.size = size;
        this.hWords = hWords;
        this.vWords = vWords;
    }

    /**
     * Returns the size of the words in the puzzle
     * 
     * @return the size of the words in the puzzle
     */
    public int size() {
        return this.size;
    }

    /**
     * Returns the number of words in the puzzle
     * 
     * @return the number of words in the puzzle
     */
    public int nrWords() {
        return this.hWords.length + this.vWords.length;
    }

    /**
     * Returns the number of swaps that must be made in the puzzle to obtain an
     * initial grid
     * 
     * @return the number of swaps that must be made in the puzzle to obtain an
     *         initial grid
     */
    public int shuffleSwaps() {
        return 5 * (this.size - 3);
    }

    /**
     * Returns the i-th letter of the j-th horizontal word
     * 
     * @param i an integer
     * @param j an integer
     * @requires {@code i > 0 && i <= size()}
     * @requires {@code j > 0 && j <= nrWords()/2}
     * @return the i-th letter of the j-th horizontal word
     */
    public char getLetterInHorizontalWord(int i, int j) {
        return this.hWords[j - 1].charAt(i - 1);
    }

    /**
     * Returns the i-th letter of the j-th vertical word
     * 
     * @param i an integer
     * @param j an integer
     * @requires {@code i > 0 && i <= size()}
     * @requires {@code j > 0 && j <= nrWords()/2}
     * @return the i-th letter of the j-th vertical word
     */
    public char getLetterInVerticalWord(int i, int j) {
        return this.vWords[j - 1].charAt(i - 1);
    }

    /**
     * Returns a matrix with the puzzle shuffled
     * 
     * @return a matrix with the puzzle shuffled
     */
    /* public char[][] getShuffledGrid() {
        char[][] grid = fillGrid(this.size, this.hWords, this.vWords);

        Random rand = new Random();

        for (int i = 0; i < (this.size + 1) / 2; i++) {
            int rowLetterIndex = rand.nextInt(this.size - 1);
            int colLetterIndex = rand.nextInt(this.size - 1);

            int k = 2 * i;

            if (!isDiagonal(k, rowLetterIndex, this.size)) {
                if (!isDiagonal(k, rowLetterIndex + 1, this.size)) {
                    char temp = grid[k][rowLetterIndex];
                    grid[k][rowLetterIndex] = grid[k][rowLetterIndex + 1];
                    grid[k][rowLetterIndex + 1] = temp;
                } else {
                    char temp = grid[k][rowLetterIndex];
                    grid[k][rowLetterIndex] = grid[k][rowLetterIndex - 1];
                    grid[k][rowLetterIndex - 1] = temp;
                }
            }

            if (!isDiagonal(colLetterIndex, k, this.size)) {
                if (!isDiagonal(colLetterIndex + 1, k, this.size)) {
                    char temp = grid[colLetterIndex + 1][k];
                    grid[colLetterIndex][k] = grid[colLetterIndex + 1][k];
                    grid[colLetterIndex + 1][k] = temp;
                } else {
                    char temp = grid[colLetterIndex][k];
                    grid[colLetterIndex][k] = grid[colLetterIndex - 1][k];
                    grid[colLetterIndex - 1][k] = temp;
                }
            }

            int selectedLetters = 0;

            while (selectedLetters < (this.size - 3) / 2) {
                int col = rand.nextInt(this.size - 1);

                if (!isDiagonal(k, col, this.size)) {
                    char temp = grid[k][col];
                    grid[k][col] = grid[col][k];
                    grid[col][k] = temp;

                    selectedLetters++;
                }
            }
        }

        return grid;
    } */

    /**
     * Returns a matrix with the puzzle shuffled, with at least 80% of the swaps being between correct letters
     * 
     * @return a matrix with the puzzle shuffled, with at least 80% of the swaps being between correct letters
     */
    public char[][] getShuffledGrid() {
        char[][] grid = fillGrid(this.size, this.hWords, this.vWords);

        Puzzle puzzle = new Puzzle(this.size, this.hWords, this.vWords);
        Random rand = new Random();
        WaffleGame game = new WaffleGame(puzzle, grid);

        int correctSwaps = 0;

        for (int i = 0; i < (this.size + 1) / 2; i++) {
            int rowLetterIndex = rand.nextInt(this.size - 1);
            int colLetterIndex = rand.nextInt(this.size - 1);

            int k = 2 * i;

            if (!isDiagonal(k, rowLetterIndex, this.size) 
            && game.clue(k+1, rowLetterIndex+1) == LetterStatus.CORRECT_POS) {
                if (!isDiagonal(k, rowLetterIndex + 1, this.size) 
                && game.clue(k+1, rowLetterIndex+2) == LetterStatus.CORRECT_POS) {
                    char temp = grid[k][rowLetterIndex];
                    grid[k][rowLetterIndex] = grid[k][rowLetterIndex + 1];
                    grid[k][rowLetterIndex + 1] = temp;

                    correctSwaps++;
                } else if (game.clue(k+1, rowLetterIndex) == LetterStatus.CORRECT_POS) {
                    char temp = grid[k][rowLetterIndex];
                    grid[k][rowLetterIndex] = grid[k][rowLetterIndex - 1];
                    grid[k][rowLetterIndex - 1] = temp;

                    correctSwaps++;
                }
            }

            if (!isDiagonal(colLetterIndex, k, this.size) 
            && game.clue(colLetterIndex+1, k+1) == LetterStatus.CORRECT_POS) {
                if (!isDiagonal(colLetterIndex + 1, k, this.size) 
                && game.clue(colLetterIndex+2, k+1) == LetterStatus.CORRECT_POS) {
                    char temp = grid[colLetterIndex + 1][k];
                    grid[colLetterIndex][k] = grid[colLetterIndex + 1][k];
                    grid[colLetterIndex + 1][k] = temp;

                    correctSwaps++;
                } else if(game.clue(colLetterIndex, k+1) == LetterStatus.CORRECT_POS) {
                    char temp = grid[colLetterIndex][k];
                    grid[colLetterIndex][k] = grid[colLetterIndex - 1][k];
                    grid[colLetterIndex - 1][k] = temp;

                    correctSwaps++;
                }
            }

            int selectedLetters = 0;

            while (selectedLetters < (this.size - 3) / 2) {
                int col = rand.nextInt(this.size - 1);

                if (!isDiagonal(k, col, this.size)) {
                    if (game.clue(k+1, col+1) == LetterStatus.CORRECT_POS && game.clue(col+1, k+1) == LetterStatus.CORRECT_POS) {
                        char temp = grid[k][col];
                        grid[k][col] = grid[col][k];
                        grid[col][k] = temp;

                        selectedLetters++;
                        correctSwaps++;
                    }
                }
            }
        }

        int totalSwaps = 100 * correctSwaps / 80;
        int swapsLeft = totalSwaps - correctSwaps;

        // does the normal shuffleSwap function simplified if make it as close as possible to 80% (always 80% or more correct swaps)
        for (int i = 0; i < (this.size + 1) / 2; i++) {
            if (swapsLeft < shuffleSwaps() - correctSwaps) {
            
                int rowLetterIndex = rand.nextInt(this.size - 1);
    
                int k = 2 * i;
    
                if (!isDiagonal(k, rowLetterIndex, this.size)) {
                    if (!isDiagonal(k, rowLetterIndex + 1, this.size)) {
                        char temp = grid[k][rowLetterIndex];
                        grid[k][rowLetterIndex] = grid[k][rowLetterIndex + 1];
                        grid[k][rowLetterIndex + 1] = temp;

                        swapsLeft++;
                    } else {
                        char temp = grid[k][rowLetterIndex];
                        grid[k][rowLetterIndex] = grid[k][rowLetterIndex - 1];
                        grid[k][rowLetterIndex - 1] = temp;

                        swapsLeft++;
                    }
                }
            }
        }

        return grid;
    } 

}
