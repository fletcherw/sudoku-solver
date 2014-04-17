import java.util.Random;  
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class SudokuMain {
   public static void main(String[] args) {
      Scanner f = null;
      Random r = new Random();
      
      int index = r.nextInt(244); //Magic number; the number of sudoku configurations in sudoku.txt
      
      try {
         f = new Scanner(new File("sudoku.txt"));
      }   
      catch (FileNotFoundException e) {
         System.out.println(e);   
      }
      
      int ii = index; //get the indexth sudoku;
      while (ii > 0) {
         f.nextLine();
         ii--;
      }
      
      Sudoku board = new Sudoku(f.nextLine());
      
      System.gc();
      long startTime = System.nanoTime();
      boolean solved = board.solve();
      int duration = (int) (System.nanoTime() - startTime)/1000;
      System.out.println(board.formatSolution());
      System.out.printf("Sudoku %d %s solved in %d microseconds. %n", index + 1, (solved ? "sucessfully" : "unsuccessfully"), duration);
   }
}