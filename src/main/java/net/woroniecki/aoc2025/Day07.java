package net.woroniecki.aoc2025;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayDeque;
import java.util.Deque;


public class Day07 {

    Block[][] grid;
    final int ROWS, COLS;

    long timelines;
    long splits;

    public Day07(String input) {
        String[] lines = input.trim().split("\n");
        grid = new Block[lines.length][];
        for (int r = 0; r < lines.length; r++) {
            String line = lines[r];
            char[] chrs = line.trim().toCharArray();
            grid[r] = new Block[chrs.length];
            for (int c = 0; c < chrs.length; c++) {
                char ch = chrs[c];
                grid[r][c] = new Block(r, c, ch, 0);
            }
        }
        ROWS = grid.length;
        COLS = grid[0].length;

        dfs();
    }

    public long part1() {
        return splits;
    }

    public long part2() {
        return timelines;
    }

    void dfs() {
        Block start = findS();
        start.below().count = 1;

        Deque<Block> queue = new ArrayDeque<>();
        queue.addLast(start.below());

        splits = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                Block current = queue.removeFirst();

                Block below = current.below();
                if (below != null) {
                    if (below.ch == '.') {
                        below.ch = '|';
                        below.count = current.count;
                        queue.addLast(below);
                    } else if (below.ch == '^') {
                        Block left = below.left();
                        splits++;
                        if (left != null) {
                            if (left.ch == '.') {
                                left.ch = '|';
                                left.count = current.count;
                                queue.addLast(left);
                            } else if (left.ch == '|') {
                                left.count += current.count;
                            }
                        }
                        Block right = below.right();
                        if (right != null) {
                            if (right.ch == '.') {
                                right.ch = '|';
                                right.count = current.count;
                                queue.addLast(right);
                            } else if (right.ch == '|') {
                                right.count += current.count;
                            }
                        }
                    } else if (below.ch == '|') {
                        below.count += current.count;
                    }
                }
            }
        }

        timelines = 0;
        for (Block block : grid[ROWS - 1]) {
            timelines += block.count;
        }
    }

    Block findS() {
        for (Block[] row : grid) {
            for (Block b : row) {
                if (b.ch == 'S')
                    return b;
            }
        }
        return null;
    }


    @ToString
    @AllArgsConstructor
    class Block {
        int r, c;
        char ch;
        long count;

        Block below() {
            if (r + 1 < ROWS)
                return grid[r + 1][c];
            else return null;
        }

        Block right() {
            if (c - 1 >= 0)
                return grid[r][c - 1];
            else return null;
        }

        Block left() {
            if (c + 1 < COLS)
                return grid[r][c + 1];
            else return null;
        }
    }

}
