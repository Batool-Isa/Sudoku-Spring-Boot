package com.ga.sudoku;

import com.ga.sudoku.model.SudokuCell;
import com.ga.sudoku.service.Sudoku;
import com.ga.sudoku.service.SudokuSolver;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SudokuApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SudokuApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Sudoku sudoku = new Sudoku();
        SudokuCell[][] grid = sudoku.readPuzzle("puzzle1.txt");
        System.out.println("----- puzzle 1-----");
        if (SudokuSolver.solve(grid)) {
            sudoku.printGameBoard(grid);
            sudoku.saveSolution("puzzle1.txt", grid);
        } else {
            System.out.println("No solution");
        }

        SudokuCell[][] grid2 = sudoku.readPuzzle("puzzle2.txt");
        System.out.println("----- puzzle 2-----");
        if (SudokuSolver.solve(grid)) {
            sudoku.printGameBoard(grid);
            sudoku.saveSolution("puzzle2.txt", grid2);
        } else {
            System.out.println("No solution");
        }

    }
}
