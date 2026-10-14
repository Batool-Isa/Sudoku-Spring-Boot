package com.ga.sudoku;

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
SudokuCell[][] grid =sudoku.readPuzzle("puzzles/puzzle1.txt");
		if (SudokuSolver.solve(grid)) {
			 sudoku.printGameBoard(grid);
		 sudoku.saveSolution();
			} else {
			System.out.println("No solution");
		}

	}
}
