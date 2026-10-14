package com.ga.sudoku;

import com.ga.sudoku.exception.InvalidCharacterException;
import com.ga.sudoku.exception.SudokuFileNotFoundException;
import org.apache.coyote.BadRequestException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Sudoku {
    private SudokuCell[][] sudokuGrid = new SudokuCell[9][9];

    // Method to read the puzzle1.txt file and generate sudoku grid
    public void readPuzzle(String fileName) throws SudokuFileNotFoundException {
        // read puzzle file
        File file = new File(fileName);
        if (!file.exists()) {
            throw new SudokuFileNotFoundException("File " + fileName + "  not found");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCounter = 0;
            int[][] sudokuCells = new int[9][9];
            int rowCounter =0;
            int columnCounter =0;
            // validate file content
            while ((line = reader.readLine()) != null) {
                lineCounter++;
                if (line.length() != 9) {
                    throw new InvalidCharacterException("Line " + lineCounter + " has " + line.length() + " character instead of 9");
                }
                columnCounter=0;

                for (char c : line.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        throw new InvalidCharacterException("Line " + lineCounter + " has " + c + " character which is not a digit");
                    }
                    sudokuGrid[rowCounter][columnCounter] = new SudokuCell(rowCounter, columnCounter,c - '0');
                    columnCounter++;

                }
                rowCounter++;
            }
            // print puzzle board
            printGameBoard();
        } catch (IOException e) {

        }


    }

    // Method to save the generated solution file as puzzle1.solution.txt
    public void saveSolution() {

    }

    //    Method to print the current game board
    public void printGameBoard() throws BadRequestException {
        if (sudokuGrid== null) {
            throw new BadRequestException("Invalid board");
        }
        // row validation
        if (sudokuGrid.length!= 9) {
            throw new BadRequestException("Invalid board");
        }
        // loop throw each row to validate column
        for (int i = 0; i < sudokuGrid.length; i ++) {
            if(sudokuGrid[i].length != 9){
                throw new BadRequestException("Invalid board");

            }

        }
        int lineCounter = 0;
        int charCounter = 0;
        System.out.println(); // for organizing the print
        for (SudokuCell[] row : sudokuGrid) {
            lineCounter++;
            for(SudokuCell cell: row) {
                charCounter++;
                System.out.print(" "+ cell.getValue() + " ");
                if (charCounter % 3 == 0 && charCounter%9 !=0) {
                    System.out.print(" | ");
                }
                if (charCounter % 9 == 0) {
                    System.out.println();
                }
            }
            if(lineCounter%3==0){
                System.out.println("----------------------------------");
            }
        }
    }
}