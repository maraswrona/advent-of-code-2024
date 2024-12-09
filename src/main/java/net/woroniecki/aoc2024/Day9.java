package net.woroniecki.aoc2024;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day9 {

    @Getter
    private final List<Integer> files;

    public Day9(String input) {
        this.files = unpack(input);
    }

    private List<Integer> unpack(String input) {
        List<Integer> files = new LinkedList<>();
        int id = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int blockCount = Integer.parseInt(String.valueOf(c));
            if (i % 2 == 0) {
                // file
                for (int j = 0; j < blockCount; j++) {
                    files.add(id);
                }
                id++;
            } else {
                // empty
                for (int j = 0; j < blockCount; j++) {
                    files.add(-1);
                }
            }
        }
        return new ArrayList<>(files);
    }

    public void defrag() {
        int left = 0;
        int right = files.size() - 1;

        while (left < right) {
            // seek empty block
            while (files.get(left) != -1) {
                left++;
            }

            // seek file to move
            while (files.get(right) == -1) {
                right--;
            }

            if (left >= right) {
                break;
            }

            // swap
            files.set(left, files.get(right));
            files.set(right, -1);
        }
    }

    public long checksum() {
        long sum = 0;
        for (int i = 0; i < files.size(); i++) {
            long id = files.get(i);
            long mul = id != -1 ? i * id : 0;
            sum += mul;
        }
        return sum;
    }

    public long part1() {
        defrag();
        return checksum();
    }


    public int part2() {
        return 0;
    }

}
