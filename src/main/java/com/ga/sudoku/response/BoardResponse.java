package com.ga.sudoku.response;

import com.ga.sudoku.model.SudokuCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardResponse {
    private SudokuCell[][] grid;
    private String puzzleName;
}
