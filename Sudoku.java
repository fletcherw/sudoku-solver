import java.util.Arrays;

public class Sudoku {
   private int[][] master = new int[9][9];
   private int[][] board = new int[9][9];
   private boolean[][][] allowed = new boolean[9][9][9];
   
   public Sudoku(int[][] input) {
      clearAllowed();
      
      for (int row = 0; row < 9; row++) {
         for (int col = 0; col < 9; col++) {
            int val = input[row][col];
            if (val == 0) {
               board[row][col] = 0;
            } else {
               set(val, row, col);
            }
         }
      }
      
      copyArray(board, master);
   }
   
   public int getBoard(int row, int col) {
      return board[row][col];
   }
   
   public boolean getAllowed(int val, int row, int col) {
      return allowed[row][col][val];
   }
   
   public Sudoku(String input) {
      clearAllowed()   ;
      
      for (int row = 0; row < 9; row++) {
         for (int col = 0; col < 9; col++) {
            int val = Character.digit(input.charAt((9 * row) + col), 10);;
            if (val == 0) {
               board[row][col] = 0;
            } else {
               set(val, row, col);
            }
         }
      }
      
      copyArray(board, master);
   }
   
   public void clearAllowed() {
      for (int ii = 0; ii < 9; ii++) {
         for (int jj = 0; jj < 9; jj++) {
            for (int kk = 0; kk < 9; kk++) {
               allowed[ii][jj][kk] = true;
            }
         }
      }  
   }
   
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
   
   public void copyArray(int[][] from, int[][]  to) {
      for(int ii = 0; ii < from.length; ii++) {
        int[] aMatrix = from[ii];
        int   aLength = aMatrix.length;
        to[ii] = new int[aLength];
        System.arraycopy(aMatrix, 0, to[ii], 0, aLength);
      }
   }
   
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
      
   public String formatSolution() {
      String formatted = "";
      for (int row = 0; row < 9; row++) {
         if (row % 3 == 0) {formatted += "+ - - - + - - - + - - - +     + - - - + - - - + - - - +\n";}
         for (int col = 0; col < 9; col++) {
           if (col % 3 == 0) {formatted += "| ";}
           formatted += (master[row][col] == 0 ? "." : master[row][col]);
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

   
   public boolean check(int val, int row, int col) {
      return allowed[row][col][val-1];
   }
   
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
                     Sudoku temp = new Sudoku(board);
                     temp.set(ii + 1, row, col);
                     if (temp.solve()) {
                        copyToThis(temp);
                        return true;
                     }   
                  }
               }
            }
         }
      }
      
      return false;
   }
   
   public boolean solve() {
      simpleSolve();
      if (isStuck()) {return false;}
      if (isSolved()) {return true;} else {
         return backtrack();
      }
       
   }
   
   public boolean simpleSolve() {
      boolean progress = false;
      boolean flag = true;
      while (flag) {
         flag = solveOne();
         flag = solveTwo() || flag;
         if (flag) {
            progress = true;
         }
      } 
      
      return progress;
   }
   
   public boolean isSolved(){
   	for (int row = 0; row < 9; row++){
   		for (int col = 0; col < 9; col++){
   			if (board[row][col] == 0){
   				return false;
   			}
   		}
   	}
   	return true;
   }
   
   public void set(int val, int row, int col) {
      board[row][col] = val;
      
      //row and column
      for (int ii = 0; ii < 9; ii++) {
         allowed[row][ii][val-1] = false;
         allowed[ii][col][val-1] = false;
      }
      
      //the 3x3
      int rowStart = row - (row % 3);
      int colStart = col - (col % 3);
      for (int ii = rowStart; ii < rowStart + 3; ii++) {
         for (int jj = colStart; jj < colStart + 3; jj++) {
            allowed[ii][jj][val-1] = false;
         }
      }
      
      //this square
      for (int ii = 0; ii < 9; ii++) {
         allowed[row][col][ii] = false;
      }
      allowed[row][col][val-1] = true;
   }

   
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
   
   public boolean solveTwo() {
      boolean progress = false;
      
      //rows
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
      
      //cols
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
      
      //squares
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