import java.util.Arrays;

public class Sudoku {
   private int[][] original = new int[9][9];
   private int[][] board = new int[9][9];
   private boolean[][][] allowed = new boolean[9][9][9];
   
   /**
    * Constructor to create a new Sudoku object from an array of Sudoku values, with 0 representing a blank.
    *
    * @param input The Sudoku array to copy.
    */
   public Sudoku(int[][] input, boolean[][][] inputAllowed) {
      
      for (int row = 0; row < 9; row++) {
         for (int col = 0; col < 9; col++) {
            int val = input[row][col];
            if (val == 0) {
               board[row][col] = 0; //0 is simply a placeholder for an empty space, so we shouldn't change what is and isn't allowed in the array.
            } else {
               set(val, row, col);
            }
            for (int ii = 0; ii < 9; ii++) {
               allowed[row][col][ii] = inputAllowed[row][col][ii];
            }
         }
      }
      
      copyArray(board, original); //record the original configuration of the board for displaying the solved and unsolved versions later.
   }
   
   /**
    * Constructor for a new Sudoku object from a 81 character string representing all of the values in the grid, 0 representing a blank.
    *
    * @param input The Sudoku array to import.
    */
   public Sudoku(String input) {
      clearAllowed();
      
      for (int row = 0; row < 9; row++) {
         for (int col = 0; col < 9; col++) {
            int val = Character.digit(input.charAt((9 * row) + col), 10);
            if (val == 0) {
               board[row][col] = 0; //0 is simply a placeholder for an empty space, so we shouldn't change what is and isn't allowed in the array.
            } else {
               set(val, row, col);
            }
         }
      }
      
      copyArray(board, original); //record the original configuration of the board for displaying the solved and unsolved versions later.
   }
   
   /**
    * Returns the value held in the board at a certain index.
    *
    * @return the held value.
    */
   public int getBoard(int row, int col) {
      return board[row][col];
   }
   
   /**
    * Returns true if a number is valid in a particular square, otherwise returns false.
    *
    * @return true if valid, otherwise false.
    */
   public boolean getAllowed(int val, int row, int col) {
      return allowed[row][col][val];
   }
   
   /**
    * Sets the array of allowed values to all true.
    */
   public void clearAllowed() {
      for (int ii = 0; ii < 9; ii++) {
         for (int jj = 0; jj < 9; jj++) {
            for (int kk = 0; kk < 9; kk++) {
               allowed[ii][jj][kk] = true;
            }
         }
      }  
   }
   
   /**
    * Replaces the board and allowed values of this Sudoku with those held in a different Sudoku object.
    *
    * @param s the Sudoku object to copy from.
    */
   public void copyToThis(Sudoku s) {
      for (int ii = 0; ii < 9; ii++) {
         for (int jj = 0; jj < 9; jj++) {
            board[ii][jj] = s.getBoard(ii, jj);
            for (int kk = 0; kk < 9; kk++) {
               allowed[ii][jj][kk] = s.getAllowed(kk, ii, jj);
            }
         }
      } 
   }
   
   /**
    * Copies all of the values in a two dimensional array to a specified array.
    */
   public void copyArray(int[][] from, int[][] to) {
      for(int ii = 0; ii < from.length; ii++) {
        int[] aMatrix = from[ii];
        int   aLength = aMatrix.length;
        to[ii] = new int[aLength];
        System.arraycopy(aMatrix, 0, to[ii], 0, aLength);
      }
   }
   
   /**
    * Returns a nicely formatted String representation of the current status of the puzzle.
    */
   public String formatSudoku() {
      String formatted = "";
      for (int row = 0; row < 9; row++) {
         if (row % 3 == 0) {formatted += "+ - - - + - - - + - - - + \n";}
         for (int col = 0; col < 9; col++) {
           if (col % 3 == 0) {formatted += "| ";}
           formatted += (board[row][col] == 0 ? "." : board[row][col]);
           formatted += " ";
         }
         formatted += "|\n";
      }
      formatted += "+ - - - + - - - + - - - + \n";
      
      return formatted;
   }
   
   /**
    * Returns a nicely formatted String representation of the initial and current status of the puzzle.
    */
   public String formatSolution() {
      String formatted = "";
      for (int row = 0; row < 9; row++) {
         if (row % 3 == 0) {formatted += "+ - - - + - - - + - - - +     + - - - + - - - + - - - +\n";}
         for (int col = 0; col < 9; col++) {
           if (col % 3 == 0) {formatted += "| ";}
           formatted += (original[row][col] == 0 ? "." : original[row][col]);
           formatted += " ";
         }
         formatted += "|     ";
         for (int col = 0; col < 9; col++) {
           if (col % 3 == 0) {formatted += "| ";}
           formatted += (board[row][col] == 0 ? "." : board[row][col]);
           formatted += " ";
         }
         formatted += "|\n";
      }
      formatted += "+ - - - + - - - + - - - +     + - - - + - - - + - - - +\n";
      
      return formatted;

   }

   /**
    * Returns true if a given value is valid in the specified location, differs from getAllowed() in that this adjusts for the 0 indexing of the allowed array.
    */
   public boolean check(int val, int row, int col) {
      return allowed[row][col][val-1];
   }
   
   /**
    * Returns true if there is any square in the board which has no possible values, otherwise returns false.
    */
   public boolean isStuck() {
      for (int row = 0; row < 9; row++) {
         for (int col = 0; col < 9; col++) {
            if (board[row][col] == 0) {
               boolean count = false;
               for (int ii = 0; ii < 9; ii++) {
                  count |= allowed[row][col][ii];
               }
               if (!count) return true;
            }
         }
      }
      
      return false;
   }
   
   public boolean backtrack() {
      if (isStuck()) {return false;}
      
      for (int row = 0; row < 9; row++) {
         for (int col = 0; col < 9; col++) {
            if (board[row][col] == 0) {
               for (int ii = 0; ii < 9; ii++) {
                  if (allowed[row][col][ii]) {
                     Sudoku temp = new Sudoku(board, allowed);
                     temp.set(ii + 1, row, col);
                     if (temp.solve()) {
                        copyToThis(temp);
                        return true;
                     }   
                  }
               }
               
               return false;
            }
         }
      }
      
      return false;
   }
   
   /**
    * Attempts to solve this Sudoku by first using simple solution methods, and then using a recursive backtrack algorithm.
    *
    * @return returns true if the Sudoku was sucessfully solved, otherwise returns false.
    */
   public boolean solve() {
      simpleSolve();
      if (isStuck()) {return false;}
      if (isComplete()) {return true;} else {
         return backtrack();
      }
       
   }
   
   
   /**
    * Repeatedly applies both simple solution methods until no progress is being made.
    */
   public void simpleSolve() {
      boolean progress = true;
      while (progress) {
         progress = solveOne();
         progress |= solveTwo();
      } 
   }
   
   /** 
    * Returns true if the board is completely filled in, otherwise returns false;
    */
   public boolean isComplete(){
   	for (int row = 0; row < 9; row++){
   		for (int col = 0; col < 9; col++){
   			if (board[row][col] == 0){
   				return false;
   			}
   		}
   	}
   	return true;
   }
   
   /**
    * Sets a certain cell to a value, and then adjusts the allowed array to reflect this change.
    */
   public void set(int val, int row, int col) {
      board[row][col] = val;
      
      //No 2 the same in row and column
      for (int ii = 0; ii < 9; ii++) {
         allowed[row][ii][val-1] = false;
         allowed[ii][col][val-1] = false;
      }
      
      //No 2 the same in the 3x3
      int rowStart = row - (row % 3);
      int colStart = col - (col % 3);
      for (int ii = rowStart; ii < rowStart + 3; ii++) {
         for (int jj = colStart; jj < colStart + 3; jj++) {
            allowed[ii][jj][val-1] = false;
         }
      }
      
      //You can't put anything else in this square.
      for (int ii = 0; ii < 9; ii++) {
         allowed[row][col][ii] = false;
      }
      allowed[row][col][val-1] = true;
   }

   /**
    * Applies a simple solution algorithm; if there is only one allowed value for a square, then it must go in that square.
    *
    * @return returns true if any squares were solved, otherwise false.
    */
   public boolean solveOne() {
      boolean progress = false;
      for (int row = 0; row < 9; row++) {
         for (int col = 0; col < 9; col++) {
            if (board[row][col] == 0) {
               int count = 0;
               int val = 0;
               for (int ii = 1; ii <= 9; ii++) {
                  if (check(ii, row, col)) {
                     val = ii;
                     count++;
                  } 
               }
               if (count == 1) {
                  set(val, row, col);
                  progress = true;
               }
            }
         }
      }
      
      return progress; 
   }
   
   /**
    * Applies a simple solution algorithm; if there is only square in a row, column, or 3x3 where a value is allowed, that value must go there.
    *
    * @return returns true if any squares were solved, otherwise false.
    */
   public boolean solveTwo() {
      boolean progress = false;
      
      //checking the rows
      for (int row = 0; row < 9; row++) {
         int[] count = new int[9];
         for (int col = 0; col < 9; col++) {
            if (board[row][col] == 0) {
               for (int ii = 0; ii < 9; ii++) {
                  if (allowed[row][col][ii]) {
                     count[ii]++;
                  }
               }
            } else {
               count[board[row][col] - 1] = 10;
            }
         }
         
         for (int ii = 0; ii < 9; ii++) {
            if (count[ii] == 1) {
               for (int col = 0; col < 9; col++) {
                  if (allowed[row][col][ii]) {
                     set(ii+1, row, col);
                     progress = true;
                  }
               }   
            }
         }
      }
      
      //checking the columns
      for (int col = 0; col < 9; col++) {
         int[] count = new int[9];
         for (int row = 0; row < 9; row++) {
            if (board[row][col] == 0) {
               for (int ii = 0; ii < 9; ii++) {
                  if (allowed[row][col][ii]) {
                     count[ii]++;
                  }
               }
            } else {
               count[board[row][col] - 1] = 10;
            }
         }
         
         for (int ii = 0; ii < 9; ii++) {
            if (count[ii] == 1) {
               for (int row = 0; row < 9; row++) {
                  if (allowed[row][col][ii]) {
                     set(ii+1, row, col);
                     progress = true;
                  }
               }   
            }
         }
      }
      
      //checking the 3x3 squares
      for (int x = 0; x < 9; x += 3) {
         for (int y = 0; y < 9; y += 3) {
            int[] count = new int[9];
            for (int row = x; row < x + 3; row++) {
               for (int col = y; col < y + 3; col++) {
                  if (board[row][col] == 0) {
                     for (int ii = 0; ii < 9; ii++) {
                        if (allowed[row][col][ii]) {
                           count[ii]++;
                        }
                     }
                  } else {
                     count[board[row][col] - 1] = 10;
                  }
               }
            }
            
            for (int ii = 0; ii < 9; ii++) {
               if (count[ii] == 1) {
                  for (int row = x; row < x + 3; row++) {
                     for (int col = y; col < y + 3; col++) {
                        if (allowed[row][col][ii]) {
                           set(ii+1, row, col);
                           progress = true;
                        }
                     }
                  }
               }
            }
         }
      }
      
      return progress;  
   }
}