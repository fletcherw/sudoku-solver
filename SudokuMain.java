import java.util.Random;  //003020600900305001001806400008102900700000008006708200002609500800203009005010300
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;

public class SudokuMain {
   public static void main(String[] args) {
      Scanner f = null;
      
      try {
         f = new Scanner(new File("sudoku.txt"));
      }   
      catch (FileNotFoundException e) {
         System.out.println(e);   
      }
      
      while (f.hasNext()) {
         Sudoku board = new Sudoku(f.nextLine());
         if (board.solve()) {
            System.out.println(board.formatSolution());
         }   
      }   
   }
}