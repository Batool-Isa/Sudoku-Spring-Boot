package com.ga.sudoku.controller;

import com.ga.sudoku.exception.InvalidBoardException;
import com.ga.sudoku.exception.InvalidCharacterException;
import com.ga.sudoku.exception.SudokuFileNotFoundException;
import com.ga.sudoku.model.SudokuCell;
import com.ga.sudoku.request.MoveRequest;
import com.ga.sudoku.response.BoardResponse;
import com.ga.sudoku.service.Sudoku;
import com.ga.sudoku.service.SudokuSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping("/api/sudoku")
@CrossOrigin(origins = "http://localhost:5173")
public class SudokuController {
    @Autowired
    private Sudoku sudoku;
    @Autowired
    private SudokuSolver solver;

    @GetMapping("/loadBoard")
    public BoardResponse loadSudoku() throws SudokuFileNotFoundException, InvalidCharacterException {
        Random random = new Random();
        int number = random.nextInt(3) + 1;
        String puzzleName = "puzzle" + number + ".txt";
        System.out.println("PUZZZLE "+puzzleName);
        return new BoardResponse(sudoku.readPuzzle(puzzleName), puzzleName);
    }


    @PostMapping("/isValid")
    public boolean isValid(@RequestBody MoveRequest move) {
        return solver.isValid(move);
    }

    @PostMapping("/isSolved")
    public boolean isSolved(@RequestBody int[][] board) {
        return solver.isGameSolved(solver.createGrid(board));
    }

    @PostMapping("/solve")
    public int[][] solve(@RequestBody int[][] board, @RequestParam String puzzleName) throws IOException {
        SudokuCell[][] grid = solver.createGrid(board);
        if (!solver.solve(grid)) {
            throw new InvalidBoardException("This puzzle has no solution");
        }
        sudoku.saveSolution(puzzleName, grid);
        return solver.changeToNumbers(grid);
    }
}
