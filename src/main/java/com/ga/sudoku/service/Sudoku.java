package com.ga.sudoku.service;

import com.ga.sudoku.exception.InvalidCharacterException;
import com.ga.sudoku.exception.SudokuFileNotFoundException;
import com.ga.sudoku.model.SudokuCell;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class Sudoku {

    // Method to read the puzzle1.txt file and generate sudoku grid
    public SudokuCell[][] readPuzzle(String fileName) throws SudokuFileNotFoundException, InvalidCharacterException {
        SudokuCell[][] sudokuGrid = new SudokuCell[9][9];
        // read puzzle file
        File file = new File("puzzles/" + fileName);
        if (!file.exists()) {
            throw new SudokuFileNotFoundException("File " + fileName + "  not found");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCounter = 0;
            int rowCounter = 0;
            int columnCounter = 0;
            // validate file content
            while ((line = reader.readLine()) != null) {
                lineCounter++;
                if (line.length() != 9) {
                    throw new InvalidCharacterException("Line " + lineCounter + " has " + line.length() + " character instead of 9");
                }
                columnCounter = 0;

                for (char c : line.toCharArray()) {
                    if (c < '0' || c > '9') {
                        throw new InvalidCharacterException("Line " + lineCounter + " has " + c + " character which is not a digit");
                    }
                    sudokuGrid[rowCounter][columnCounter] = new SudokuCell(rowCounter, columnCounter, c - '0');
                    columnCounter++;

                }
                rowCounter++;
            }
            if (rowCounter != 9) {
                throw new InvalidCharacterException("File has " + rowCounter + " lines instead of 9");
            }
            // print puzzle board
            printGameBoard(sudokuGrid);
            return sudokuGrid;
        } catch (IOException e) {
            throw new RuntimeException("Could not read file " + fileName, e);
        }


    }

    // Method to save the generated solution file as puzzle1.solution.txt
    public void saveSolution(String puzzleFileName, SudokuCell[][] grid) throws IOException {

        try {
            String fileName = puzzleFileName.replace(".txt", "");
            File file = new File("puzzles-solutions/" + fileName + "-solution.txt");
            file.getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            System.out.println("Calling writere");
            for (SudokuCell[] row : grid) {
                for (SudokuCell cell : row) {
                    writer.write(String.valueOf(cell.getValue()));

                }
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //    Method to print the current game board
    public void printGameBoard(SudokuCell[][] sudokuGrid) throws BadRequestException {
        if (sudokuGrid == null) {
            throw new BadRequestException("Invalid board");
        }
        // row validation
        if (sudokuGrid.length != 9) {
            throw new BadRequestException("Invalid board");
        }
        // loop throw each row to validate column
        for (int i = 0; i < sudokuGrid.length; i++) {
            if (sudokuGrid[i].length != 9) {
                throw new BadRequestException("Invalid board");

            }

        }
        int lineCounter = 0;
        int charCounter = 0;
        System.out.println(); // for organizing the print
        for (SudokuCell[] row : sudokuGrid) {
            lineCounter++;
            for (SudokuCell cell : row) {
                charCounter++;
                System.out.print(" " + cell.getValue() + " ");
                if (charCounter % 3 == 0 && charCounter % 9 != 0) {
                    System.out.print(" | ");
                }
                if (charCounter % 9 == 0) {
                    System.out.println();
                }
            }
            if (lineCounter % 3 == 0) {
                System.out.println("----------------------------------");
            }
        }
    }
}