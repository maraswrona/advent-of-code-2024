package net.woroniecki.aoc2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Setter;

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

        public boolean hasLBox() {
            return ch == '[';
        }

        public boolean hasRBox() {
            return ch == ']';
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

    public Day15(String input, boolean part2) {
        String[] parts = input.split("(\r?\n){2}");

        String[] lines = parts[0].split("\n");
        grid = new Block[lines.length][];

        if (!part2) {
            for (int y = 0; y < lines.length; y++) {
                char[] chars = lines[y].trim().toCharArray();
                grid[y] = new Block[chars.length];
                for (int x = 0; x < chars.length; x++) {
                    char ch = chars[x];
                    grid[y][x] = new Block(x, y, ch);
                }
            }
        } else {
            for (int y = 0; y < lines.length; y++) {
                char[] chars = lines[y].trim().toCharArray();
                grid[y] = new Block[chars.length * 2];
                for (int x = 0; x < chars.length; x++) {
                    char ch = chars[x];
                    String ch2 = switch (ch) {
                        case '@' -> "@.";
                        case '.' -> "..";
                        case 'O' -> "[]";
                        case '#' -> "##";
                        default -> throw new IllegalStateException("Unexpected value: " + ch);
                    };
                    grid[y][x * 2] = new Block(x * 2, y, ch2.charAt(0));
                    grid[y][x * 2 + 1] = new Block(x * 2 + 1, y, ch2.charAt(1));
                }
            }
        }

        Set<Character> moves = Set.of('^', 'v', '<', '>');
        for (char ch : parts[1].toCharArray()) {
            if (moves.contains(ch))
                this.moves.add(ch);
        }
    }

    public int part1() {
        return completeAllMoves();
    }

    private int completeAllMoves() {
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
                .filter(b -> b.hasBox() || b.hasLBox())
                .mapToInt(block -> block.y * 100 + block.x)
                .sum();
    }

    private void tryMove(Block block, Move move) {
        int dx = move.dx();
        int dy = move.dy();

        Set<Block> toMove = new HashSet<>();
        LinkedList<Block> stack = new LinkedList<>();
        stack.push(block);

        while (!stack.isEmpty()) {
            Block b = stack.pop();
            toMove.add(b);
            int nx = b.x + dx;
            int ny = b.y + dy;
            Block next = grid[ny][nx];
            if (next.hasWall()) {
                return;
            } else if (next.hasBox()) {
                stack.push(next);
            } else if (next.hasRBox()) {
                stack.push(next);
                if(move == Move.UP || move == Move.DOWN) {
                    stack.push(grid[ny][nx-1]);
                }

            } else if (next.hasLBox()) {
                stack.push(next);
                if(move == Move.UP || move == Move.DOWN) {
                    stack.push(grid[ny][nx + 1]);
                }
            }
        }

        while (!toMove.isEmpty()) {
            Optional<Block> blockToMove = toMove.stream().filter(b -> {
                int nx = b.x + dx;
                int ny = b.y + dy;
                Block next = grid[ny][nx];
                return next.isEmpty();
            }).findFirst();
            blockToMove.ifPresent(b -> {
                int nx = b.x + dx;
                int ny = b.y + dy;
                grid[ny][nx].setCh(b.ch);
                b.setCh('.');
                toMove.remove(b);
            });
        }
    }

    public int part2() {
        return completeAllMoves();
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
