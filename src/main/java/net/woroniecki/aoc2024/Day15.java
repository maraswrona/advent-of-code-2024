package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day15 {

    final Block[][] grid;
    final List<Character> moves = new ArrayList<>();

    @AllArgsConstructor
    public static class Block {
        int x, y;

        @Setter
        char ch;

        public boolean hasRobot() {
            return ch == '@';
        }

        public boolean hasBox() {
            return ch == 'O';
        }

        public boolean hasWall() {
            return ch == '#';
        }

        public boolean isEmpty() {
            return ch == '.';
        }
    }

    public enum Move {
        UP, DOWN, LEFT, RIGHT;

        public static Move from(char ch) {
            return switch (ch) {
                case '^' -> UP;
                case '>' -> RIGHT;
                case 'v' -> DOWN;
                case '<' -> LEFT;
                default -> throw new IllegalStateException("Unexpected value: " + ch);
            };
        }

        public int dx() {
            return switch (this) {
                case UP, DOWN -> 0;
                case LEFT -> -1;
                case RIGHT -> 1;
            };
        }

        public int dy() {
            return switch (this) {
                case UP -> -1;
                case DOWN -> 1;
                case LEFT, RIGHT -> 0;
            };
        }
    }

    public Day15(String input) {
        String[] parts = input.split("(\r?\n){2}");

        String[] lines = parts[0].split("\n");
        grid = new Block[lines.length][];
        for (int y = 0; y < lines.length; y++) {
            char[] chars = lines[y].trim().toCharArray();
            grid[y] = new Block[chars.length];
            for (int x = 0; x < chars.length; x++) {
                char ch = chars[x];
                grid[y][x] = new Block(x, y, ch);
            }
        }

        Set<Character> moves = Set.of('^', 'v', '<', '>');
        for (char ch : parts[1].toCharArray()) {
            if (moves.contains(ch))
                this.moves.add(ch);
        }
    }

    public int part1() {

        for (char ch : moves) {
            Block robot = findRobot();
            Move from = Move.from(ch);
            tryMove(robot, from);
        }
        return calculateGPS();
    }


    public void move(Move m) {
        Block robot = findRobot();
        tryMove(robot, m);
    }

    public int calculateGPS() {
        return allBlocks()
                .filter(Block::hasBox)
                .mapToInt(block -> block.y * 100 + block.x)
                .sum();

    }

    private boolean tryMove(Block block, Move move) {
        int dx = move.dx();
        int dy = move.dy();

        int nx = block.x + dx;
        int ny = block.y + dy;

        if (grid[ny][nx].hasWall()) {
            return false;
        }

        if (grid[ny][nx].hasBox()) {
            boolean moved = tryMove(grid[ny][nx], move);
            if (moved) {
                grid[ny][nx].setCh(block.ch);
                block.setCh('.');
                return true;
            } else {
                return false;
            }
        }

        grid[ny][nx].setCh(block.ch);
        block.setCh('.');
        return true;

    }

    public int part2() {
        return 0;
    }

    public Block findRobot() {
        return allBlocks()
                .filter(Block::hasRobot)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("can't find robot"));

    }

    public Stream<Block> allBlocks() {
        return Arrays.stream(grid).flatMap(Arrays::stream);
    }


}
