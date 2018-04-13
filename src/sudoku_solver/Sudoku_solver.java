

package sudoku_solver;
import java.util.Scanner;
public class Sudoku_solver {


    public static void main(String[] args) {
        solver s = new solver();
        for (int ROW = 0; ROW < 9; ROW++) {
            for (int COL = 0; COL < 9; COL++) {
                s.printWithStar(ROW, COL);
                System.out.printf("Enter a value for '#', or type 0 then ENTER to continue: ");
                Scanner kb = new Scanner(System.in);
                int VAL;
                String read = kb.nextLine();
                if (read.length() > 0) {
                    VAL = Integer.parseInt(read);
                    if (VAL >= 0 && VAL <= 9) s.publicInsert(VAL, ROW, COL);
                            
                }
                
            }
        }
        
        s.print();
        s.solve();
        System.out.println();
        s.print();
    }
    
}
