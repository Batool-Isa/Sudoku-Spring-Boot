package com.ga.sudoku.controller;

import com.ga.sudoku.exception.InvalidCharacterException;
import com.ga.sudoku.exception.SudokuFileNotFoundException;
import com.ga.sudoku.model.SudokuCell;
import com.ga.sudoku.service.Sudoku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sudoku")
@CrossOrigin(origins = "http://localhost:5173")
public class SudokuController {
    @Autowired
    private Sudoku sudoku;
    @GetMapping("loadBoard")
    public SudokuCell[][] loadSudoku() throws SudokuFileNotFoundException, InvalidCharacterException {
        return sudoku.readPuzzle("puzzle1.txt");
    }
}
