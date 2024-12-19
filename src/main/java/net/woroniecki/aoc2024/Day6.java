package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day6 {

    private final Block[][] table;

    @AllArgsConstructor
    private static class Block {
        int x, y;
        boolean hasObstacle;
        boolean hasGuard;
        boolean visited;
        Direction visitedFrom;
        char ch;

        void visit(Direction from) {
            this.visited = true;
            this.ch = 'X';
            this.visitedFrom = from;
        }

        void putObstacle() {
            this.hasObstacle = true;
            this.ch = '#';
        }

        public void removeObstacle() {
            this.hasObstacle = false;
            this.ch = '.';
        }
    }

    public Day6(String input) {
        String[] lines = input.split("\n");
        table = new Block[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            char[] chars = lines[i].toCharArray();
            table[i] = new Block[chars.length];
            for (int j = 0; j < chars.length; j++) {
                char ch = chars[j];
                table[i][j] = new Block(i, j, ch == '#', ch == '^', false, null, ch);
            }
        }
    }

    public long part1() {
        completeWalk();
        return allBlocks().filter(b -> b.visited).count();
    }

    public int part2() {
        completeWalk();
        List<Block> visited = allBlocks().filter(b -> b.visited).toList();

        int obstacles = 0;
        for (int i = 0; i < visited.size(); i++) {
            Block b = visited.get(i);
            reset();
            b.putObstacle();
            WalkEnd walkEnd = completeWalk();
            if (walkEnd == WalkEnd.LOOP) {
                obstacles++;
            }
            b.removeObstacle();
        }
        return obstacles;
    }

    private WalkEnd completeWalk() {
        Block current = findGuard();
        Direction dir = Direction.NORTH;
        current.visit(dir);

        while (true) {
            Block next = next(current, dir);

            if (next == null) {
                return WalkEnd.OUT;
            } else if (next.visited && next.visitedFrom == dir) {
                return WalkEnd.LOOP;
            }

            if (next.hasObstacle) {
                dir = dir.rotateRight();
            } else {
                current = next;
                current.visit(dir);
            }
        }
    }

    private void reset() {
        allBlocks().forEach(b -> {
            b.visited = false;
            b.visitedFrom = null;
        });
    }

    private Block next(Block guard, Direction dir) {
        int x = guard.x;
        int y = guard.y;
        switch (dir) {
            case NORTH:
                x--;
                break;
            case SOUTH:
                x++;
                break;
            case EAST:
                y++;
                break;
            case WEST:
                y--;
                break;
        }
        if (checkCoords(x, y)) {
            return table[x][y];
        } else {
            return null;
        }
    }

    private boolean checkCoords(int x, int y) {
        return x >= 0 && x < table.length && y >= 0 && y < table[x].length;
    }

    public Stream<Block> allBlocks() {
        return Arrays.stream(table).flatMap(Arrays::stream);
    }

    private Block findGuard() {
        return allBlocks()
                .filter(b -> b.hasGuard)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("can't find guard"));
    }

    private enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public Direction rotateRight() {
            return Direction.values()[(this.ordinal() + 1) % Direction.values().length];
        }
    }

    private enum WalkEnd {
        OUT, LOOP;
    }
}
