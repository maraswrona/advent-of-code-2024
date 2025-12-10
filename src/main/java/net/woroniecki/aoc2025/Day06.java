package net.woroniecki.aoc2025;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Day06 {

    long[][] arr;
    long finalSum = 0;

    public Day06(String input) {
        String[] lines = input.trim().split("\n");

        int rows = lines.length - 1;

        arr = new long[rows][];

        for (int r = 0; r < rows; r++) {
            String line = lines[r];
            String[] values = line.trim().split(" +");
            arr[r] = new long[values.length];
            for (int j = 0; j < values.length; j++) {
                String value = values[j];
                long l = Long.parseLong(value.trim());
                arr[r][j] = l;
            }
        }

        int cols = arr[0].length;

        String[] ops = lines[rows].trim().split(" +");

        for (int c = 0; c < cols; c++) {
            String op = ops[c].trim();
            long res = 0;
            if (op.trim().equals("+")) {
                long sum = 0;
                for (int r = 0; r < rows; r++) {
                    sum += arr[r][c];
                }
                res = sum;
            } else if (op.trim().equals("*")) {
                long prod = 1;
                for (int r = 0; r < rows; r++) {
                    prod *= arr[r][c];
                }
                res = prod;
            }
            this.finalSum += res;
        }


    }

    public long part1() {
        return finalSum;
    }

    public long part2() {
        return 0;
    }


}
