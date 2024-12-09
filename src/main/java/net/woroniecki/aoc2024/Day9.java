package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day9 {

    private final String input;

    public Day9(String input) {
        this.input = input;
    }

    public List<Integer> unpackBlocks() {
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

    public List<Integer> defragBlocks() {
        List<Integer> result = unpackBlocks();
        int left = 0;
        int right = result.size() - 1;

        while (left < right) {
            // seek empty block
            while (result.get(left) != -1) {
                left++;
            }

            // seek file to move
            while (result.get(right) == -1) {
                right--;
            }

            if (left >= right) {
                break;
            }

            // swap
            result.set(left, result.get(right));
            result.set(right, -1);
        }
        return result;
    }

    @Value
    @AllArgsConstructor
    public static class File {
        int id;
        int size;
    }

    public List<File> unpackFiles() {
        List<File> files = new LinkedList<>();
        int id = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int blockCount = Integer.parseInt(String.valueOf(c));
            if (i % 2 == 0) {
                // file
                files.add(new File(id, blockCount));
                id++;
            } else {
                // empty
                files.add(new File(-1, blockCount));
            }
        }
        return new ArrayList<>(files);
    }

    public List<File> defragFiles() {
        List<File> files = unpackFiles();

        int i = files.size() - 1;
        while (i >= 0) {
            File file = files.get(i);
            if (file.id == -1) {
                i--;
                continue;
            }

            int spaceIdx = -1;
            for (int j = 0; j < i; j++) {
                File space = files.get(j);
                if (space.id == -1 && space.size >= file.size) {
                    spaceIdx = j;
                    break;
                }
            }

            if (spaceIdx == -1) {
                i--;
                continue;
            }

            File space = files.get(spaceIdx);
            if (space.size == file.size) {
                files.set(spaceIdx, file);
                files.set(i, space);
                i--;
            } else {
                files.set(spaceIdx, file);
                files.set(i, new File(-1, file.size));
                files.add(spaceIdx + 1, new File(-1, space.size - file.size));
            }

        }

        return files;
    }

    public long checksumBlocks(List<Integer> files) {
        long sum = 0;
        for (int i = 0; i < files.size(); i++) {
            long id = files.get(i);
            long mul = id != -1 ? i * id : 0;
            sum += mul;
        }
        return sum;
    }

    public long checksumFiles(List<File> files) {
        long sum = 0;
        long pos = 0;
        for (File file : files) {
            long id = file.id;
            for (int i = 0; i < file.size; i++) {
                long mul = id != -1 ? id * pos : 0;
                sum += mul;
                pos++;
            }
        }
        return sum;
    }

    public long part1() {
        return checksumBlocks(defragBlocks());
    }


    public long part2() {
        return checksumFiles(defragFiles());
    }

}
