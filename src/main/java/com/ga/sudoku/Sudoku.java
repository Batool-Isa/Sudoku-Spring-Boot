package com.ga.sudoku;

import com.ga.sudoku.exception.InvalidCharacterException;
import com.ga.sudoku.exception.SudokuFileNotFoundException;
import org.apache.coyote.BadRequestException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sudoku {
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
            ArrayList<Integer> num = new ArrayList<>();

            // validate file content
            while ((line = reader.readLine()) != null) {
                lineCounter++;
                if (line.length() != 9) {
                    throw new InvalidCharacterException("Line " + lineCounter + " has " + line.length() + " character instead of 9");
                }
                for (char c : line.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        throw new InvalidCharacterException("Line " + lineCounter + " has " + c + " character which is not a digit");
                    }
                    num.add(c - '0');
                }
            }
            // print puzzle board
            PrintGameBoard(num);
        } catch (IOException e) {

        }


    }

    // Method to save the generated solution file as puzzle1.solution.txt
    public void saveSolution() {

    }

    //    Method to print the current game board
    public void PrintGameBoard(ArrayList<Integer> puzzulesNum) throws BadRequestException {
        if (puzzulesNum.size() != 81) {
            throw new BadRequestException("Invalid board");
        }
        List<List<Integer>> puzzlesLines = new ArrayList<>();

        for (int i = 0; i < puzzulesNum.size(); i += 9) {
            puzzlesLines.add(puzzulesNum.subList(i, (i + 9)));

        }
        for (List<Integer> line : puzzlesLines) {
            System.out.println(line);
        }
    }
}