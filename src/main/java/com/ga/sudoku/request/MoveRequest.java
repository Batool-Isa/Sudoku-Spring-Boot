package com.ga.sudoku.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoveRequest {
    private int[][] board;
    private int row;
    private int col;
    private int value;
}
