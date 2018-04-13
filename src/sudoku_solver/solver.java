
package sudoku_solver;

import java.util.Arrays;
import java.util.Scanner;

public class solver {
    private final int GRID_SIZE = 9; // Warning: Keep at 9
    private int[][] grid = new int[GRID_SIZE][GRID_SIZE]; 
        /* The sudoku board, represented by a two-dimensional array of ints.
        grid[row][column] */
    private Boolean[][][] isPossible = new Boolean[GRID_SIZE][GRID_SIZE][GRID_SIZE];
        /* This variable determines whether or not a certain value can be inserted
        into an empty space. Initialized to false in solver constructor.
        isPossible[value - 1][row][column] 
        (The first is value - 1 because the possible values are 1-9, but in order for this to
        be applicable to an array of size 9 it needs to be 0-8) */
    private Boolean[][] isFilled = new Boolean[GRID_SIZE][GRID_SIZE];
        /* Array of bools to represent for each space, 
        whether or not a value has been inserted. Initialized to false in solver constructor.
        isFilled[row][column] */
    
    private int numFilled = 0; // The number of filled spaces on the board.
    
    private class Section {
        private int minRow;
        private int minCol;
        private int maxRow;
        private int maxCol;
        
        public Section(int row, int col) {
            /* Given a row and a column, a section is generated.
               There are nine sections on a 9x9 grid.
               minRow, maxRow, minCol, and maxCol are the boundaries
               to be used when traversing a Section using for loops */
            if (row >= 0 && row <= 2) {
                minRow = 0;
                maxRow = 2;
            }
            if (row >= 3 && row <= 5) {
                minRow = 3;
                maxRow = 5;
            }
            if (row >= 6 && row <= 8) {
                minRow = 6;
                maxRow = 8;
            }
            if (col >= 0 && col <= 2) {
                minCol = 0;
                maxCol = 2;
            }
            if (col >= 3 && col <= 5) {
                minCol = 3;
                maxCol = 5;
            }
            if (col >= 6 && col <= 8) {
                minCol = 6;
                maxCol = 8;
            }
        }
        
        public int getMiR(){
            return minRow;
        }
        public int getMaR(){
            return maxRow;
        }
        public int getMiC(){
            return minCol;
        }
        public int getMaC(){
            return maxCol;
        }
    }
    
    private void insert(int value, int row, int col) {
        /* The insert function places the given value into the given location
        on the board. Afterwards, it sets the row, column, and section of the given location
        to not possible for the given value. */
        
        if (value >= 1 && value <= 9) { // We're only dealing with 1 through 9
            grid[row][col] = value; // Set the space on the board to the given value
            for (int i = 0; i < GRID_SIZE; i++) {
                isPossible[value - 1][i][col] = false;
                isPossible[value - 1][row][i] = false;
                // Sets all isPossible values in the same row and the same column to false
            }
            Section sec = new Section(row, col); // Determines the section based on the given row and column
            for (int i = sec.getMiR(); i <= sec.getMaR(); i++) {
                for (int i2 = sec.getMiC(); i2 <= sec.getMaC(); i2++) {
                    isPossible[value - 1][i][i2] = false;
                }
            }
            isFilled[row][col] = true; // declares the location as filled, then increments number of filled spaces.
            numFilled++;
            
            /* TEST FOR HUMAN SPEED INSERTION
            print();
            Scanner kb = new Scanner(System.in);
            System.out.print("Press Enter to continue.");
            kb.nextLine();
            // END OF TEST */
                    
        }
        else throw new java.lang.Error("Only 1 through 9 can be used in the insert function");
        
    }
    
    Boolean canInsert(int value, int row, int col) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (value == grid[row][i] || value == grid[i][col]) return false; 
        }
        Section SEC = new Section(row, col);
        for (int i = SEC.getMiR(); i <= SEC.getMaR(); i++) {
            for (int i2 = SEC.getMiC(); i2 <= SEC.getMaC(); i2++) {
                if (value == grid[i][i2]) return false;
            }
        }
        return true;
    }
    public void publicInsert(int value, int row, int col) {
        if (value >= 1 && value <= 9 && canInsert(value, row, col)) insert(value, row, col);
    }
    public void solve() {
        /* The solve method is as follows:
        
            (1) For every space on the board, if it is not filled, check to see how many possible values can be placed there
                    If only one value can be placed there, insert that value there
        
            (2) For each row, check every value to see how many spaces on the row is the value available for insertion (using isPossible).
                When the first available space for the value is found, save the location of this space. (Only the column is necessary)
                Stop this subloop and move on if one of the following conditions is satisfied:
                    - The value is already in the row
                    - There is more than one possible place for the value (The moment a second available space is found)
                If the end is reached then there must only be one space, which was earlier saved
                insert the value into the saved space.
            Repeat process (2) for each column and each section.
            If it is still not solved (there are still empty spaces) then repeat processes (1) and (2)
            If the entire method has gone without a single insertion, then we know the sudoku puzzle has multiple solutions or no solutions
                and we break the loop
        */
        Boolean madeinsertion; // Whether or not an insertion was made throughout the main loop.
        do {
            madeinsertion = false;
            
            // Checking each empty space
            for (int ROW = 0; ROW < GRID_SIZE; ROW++) {
                for (int COL = 0; COL < GRID_SIZE; COL++) {
                    if (!isFilled[ROW][COL]) {
                        int numPoss = 0; // Number of possible values to be placed here
                        int savedRow = 0, savedCol = 0, savedVal = 0;
                        for (int VAL = 1; VAL <= 9; VAL++) {
                            if (isPossible[VAL - 1][ROW][COL]) { // If a possible value is found
                                numPoss++;
                                if (numPoss == 1) {
                                    savedRow = ROW;
                                    savedCol = COL;
                                    savedVal = VAL;
                                }
                                else break;
                            }
                        }
                        if (numPoss == 1) {
                            insert(savedVal, savedRow, savedCol);
                            madeinsertion = true;
                        }
                    }
                }
            }
            // Checking each row.
            for (int ROW = 0; ROW < GRID_SIZE; ROW++) {
                for (int VALUE = 1; VALUE <= 9; VALUE++) {
                    int numAvailable = 0; // The number of spaces in this area where the value is possible.
                    int savedCol = 0;
                    for (int COL = 0; COL < GRID_SIZE; COL++) {
                        if (grid[ROW][COL] == VALUE) break; // The value is already here. Move on.
                        if (!isFilled[ROW][COL] && isPossible[VALUE - 1][ROW][COL]) { // If we see an available empty space
                            numAvailable++;
                            if (numAvailable == 1) savedCol = COL; // If this is the first available space, save the column
                            else break; // If it's not the first space, move on.
                        }
                    }
                    if (numAvailable == 1) {
                        insert(VALUE, ROW, savedCol);
                        madeinsertion = true;
                    }
                }
            }
            
            // Checking each column.
            for (int COL = 0; COL < GRID_SIZE; COL++) {
                for (int VALUE = 1; VALUE <= 9; VALUE++) {
                    int numAvailable = 0; // The number of spaces in this area where the value is possible.
                    int savedRow = 0;
                    for (int ROW = 0; ROW < GRID_SIZE; ROW++) {
                        if (grid[ROW][COL] == VALUE) break; // The value is already here. Move on.
                        if (!isFilled[ROW][COL] && isPossible[VALUE - 1][ROW][COL]) { // If we see an available empty space
                            numAvailable++;
                            if (numAvailable == 1) savedRow = ROW; // If this is the first available space, save the row
                            else break; // If it's not the first space, move on.
                        }
                    }
                    if (numAvailable == 1) {
                        insert(VALUE, savedRow, COL);
                        madeinsertion = true;
                    }
                }
            }
            
            // Checking each section.
            for (int SecRow = 0; SecRow < GRID_SIZE; SecRow += 3) {
                for (int SecCol = 0; SecCol < GRID_SIZE; SecCol += 3) {
                    Section SEC = new Section(SecRow, SecCol);
                    for (int VALUE = 1; VALUE <= 9; VALUE++) {
                        int numAvailable = 0; // The number of spaces in this area where the value is possible.
                        int savedRow = 0;
                        int savedCol = 0;
                        Boolean contFor = true; // Bool for continuing the following for loop
                        for (int ROW = SEC.getMiR(); ROW <= SEC.getMaR() && contFor; ROW++) {
                            for (int COL = SEC.getMiC(); COL <= SEC.getMaC(); COL++) {
                                if (grid[ROW][COL] == VALUE) { // The value is already here. Move on.
                                    contFor = false;
                                    break;
                                }
                                if (!isFilled[ROW][COL] && isPossible[VALUE - 1][ROW][COL]) { // If we see an available empty space
                                    numAvailable++;
                                    if (numAvailable == 1) { // If this is the first available space, save the location
                                        savedRow = ROW;
                                        savedCol = COL;
                                    }
                                    else { // If it's not the first space, move on.
                                        contFor = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (numAvailable == 1) {
                            insert(VALUE, savedRow, savedCol);
                            madeinsertion = true;
                        }
                    }
                    
                }
            }
            
        } while (madeinsertion);
        if (numFilled != GRID_SIZE * GRID_SIZE) {
            System.out.println("No solution.");
        }
        else System.out.println("Solved!");
    }
    
    public void print() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int i2 = 0; i2 < GRID_SIZE; i2++) {
                if (grid[i][i2] == 0) System.out.printf("- ");
                else System.out.printf(grid[i][i2] + " ");
            }
            System.out.println();
        }
    }
    public void printWithStar(int starRow, int starCol) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int i2 = 0; i2 < GRID_SIZE; i2++) {
                if (i == starRow && i2 == starCol) System.out.printf("# ");
                else if (grid[i][i2] == 0) System.out.printf("- ");
                else System.out.printf(grid[i][i2] + " ");
            }
            System.out.println();
        }
    }
    
    public solver() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int i2 = 0; i2 < GRID_SIZE; i2++) {
                for (int i3 = 0; i3 < GRID_SIZE; i3++) {
                    isPossible[i3][i][i2] = true;
                }
                isFilled[i][i2] = false;
            }
        }
    }
}
