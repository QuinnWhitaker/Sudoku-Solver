# Sudoku-Solver
An algorithm I created to solve Sudoku puzzles

The front-end of this program (Sudoku_solver.java) allows the user to input a 9 by 9 unsolved sudoku board.
The inputed information is added in a solver class, which inplements the algorithm to solve the board.

The solve algorithm is as follows:
        
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
