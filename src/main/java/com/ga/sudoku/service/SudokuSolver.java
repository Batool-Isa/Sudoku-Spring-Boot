package com.ga.sudoku.service;

import com.ga.sudoku.exception.InvalidBoardException;
import com.ga.sudoku.model.SudokuCell;
import com.ga.sudoku.request.MoveRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SudokuSolver {
    public SudokuCell[][] createGrid(int[][] board) {
        if (board == null || board.length != 9) {
            throw new InvalidBoardException("Board must have 9 rows");
        }
        for (int[] row : board) {
            if (row == null || row.length != 9) {
                throw new InvalidBoardException("Each row must have 9 cells");
            }
        }
        SudokuCell[][] grid = new SudokuCell[9][9];
        for (int sudRow = 0; sudRow < 9; sudRow++) {
            for (int sudCol = 0; sudCol < 9; sudCol++) {
                grid[sudRow][sudCol] = new SudokuCell(sudRow, sudCol, board[sudRow][sudCol]);
            }
        }
        return grid;
    }
    public int[][] changeToNumbers(SudokuCell[][] grid) {
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = grid[r][c].getValue();
            }
        }
        return board;
    }

    public boolean isValid(MoveRequest request) {
        SudokuCell[][] grid = createGrid(request.getBoard());
        grid[request.getRow()][request.getCol()].setValue(0);
        return isValid(grid, request.getRow(), request.getCol(), request.getValue());
    }
    public boolean isValid(SudokuCell[][] grid, int row, int col, int value) {
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

    public boolean solve(SudokuCell[][] grid) {
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
    public boolean isGameSolved(SudokuCell[][] grid){
        // loop throw all rows
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
            int value = grid[row][col].getValue();
            if(value == 0 ){
                return false;
            }

                grid[row][col].setValue(0);
                boolean valid = isValid(grid, row, col, value);
                grid[row][col].setValue(value);

                if (!valid) {
                    return false;
                }
            }
        }
        return true;
    }
}
