package com.ga.sudoku.service;

import com.ga.sudoku.model.SudokuCell;

public class SudokuSolver {

    public static boolean isValid(SudokuCell[][] grid, int row, int col, int value) {
        // row check
        for (int i = 0; i < 9; i++) {
            if (grid[row][i].getValue() == (value)) {
                return false;
            }
        }
        // column check
        for (int r = 0; r < 9; r++) {
            if (grid[r][col].getValue() == (value)) {
                return false;
            }
        }
        // grid box check
        int boxRowStart = row - (row % 3);
        int boxColStart = col - (col % 3);
        for (int r = 0; r < 3; r++) {
            // System.out.println("row of grid--> "+r);
            for (int c = 0; c < 3; c++) {
                // System.out.println("col of grid--> "+r);
                if (grid[r + boxRowStart][c + boxColStart].getValue() == (value)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean solve(SudokuCell[][] grid) {
        // loop throw all rows
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // only change tha values that has zero which is considered empty
                if (grid[row][col].getValue() == 0) {
                    for (int n = 1; n <= 9; n++) {
                        if (isValid(grid, row, col, n)) {
                            grid[row][col].setValue(n);
                            if (solve(grid)) {
                                return true;
                            }
                            grid[row][col].setValue(0); // if false rollback to 0
                        }
                    }
                    // return false if all num from 0-9 failed
                    return false;
                }
            }
        }

        System.out.println("calling is valid  " + isValid(grid, 0, 4, 8));
        return true;
    }
}
