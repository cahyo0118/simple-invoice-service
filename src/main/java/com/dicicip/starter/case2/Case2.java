package com.dicicip.starter.case2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;

public class Case2 {


    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            int[][] matrix = Case2.readData();
            System.out.println(Case2.diagonalDiffCount(matrix));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int diagonalDiffCount(int[][] matrix) {
        int leftDiagonalIndex = 0;
        int rightDiagonalIndex = matrix.length - 1;

        List<Integer> leftDiagonalNumbers = new ArrayList<>();
        List<Integer> rightDiagonalNumbers = new ArrayList<>();

        for (int verticalIndex = 0; verticalIndex < matrix.length; verticalIndex++) {

            if (leftDiagonalIndex < matrix.length) {
                leftDiagonalNumbers.add(matrix[verticalIndex][leftDiagonalIndex]);
                leftDiagonalIndex += 1;
            }

            if (rightDiagonalIndex > -1) {
                rightDiagonalNumbers.add(matrix[verticalIndex][rightDiagonalIndex]);
                rightDiagonalIndex -= 1;
            }

        }

        int leftDiagonalCount = leftDiagonalNumbers.stream().mapToInt(leftDiagonalNumber -> leftDiagonalNumber).sum();
        int rightDiagonalCount = rightDiagonalNumbers.stream().mapToInt(rightDiagonalNumber -> rightDiagonalNumber).sum();

        if (leftDiagonalCount > rightDiagonalCount) {
            return leftDiagonalCount - rightDiagonalCount;
        } else {
            return rightDiagonalCount - leftDiagonalCount;
        }
    }

    public static int[][] readData() throws IOException {
        Resource resource = new ClassPathResource("matrix.txt");
        Scanner input = new Scanner(resource.getFile());

        // pre-read in the number of rows/columns
        int size = Integer.parseInt(input.nextLine());
        int[][] array = new int[size][size];
        while (input.hasNextLine()) {
            for (int i = 0; i < array.length; i++) {
                String[] line = input.nextLine().trim().split(" ");
                for (int j = 0; j < line.length; j++) {
                    array[i][j] = Integer.parseInt(line[j]);
                }
            }
        }

        input.close();

        return array;
    }

}
