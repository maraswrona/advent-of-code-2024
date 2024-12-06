package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;
import org.javatuples.Pair;

public class Day6 {

    private final char[][] table;

    public Day6(String input) {
        String[] lines = input.split("\n");
        table = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            table[i] = lines[i].toCharArray();
        }
    }

    public int part1() {
        Point guard = findGuard();

        Direction dir = Direction.NORTH;

        int visited = 1;
        while (true) {
            //printTable();
            Pair<Point, Character> ahead = blockAhead(guard, dir);

            if (ahead == null) {
                table[guard.x][guard.y] = 'X';
                visited++;
                break;
            }

            Point p = ahead.getValue0();
            char ch = ahead.getValue1();

            if (ch == '#') {
                dir = dir.rotateRight();
                continue;
            }

            if (ch == '.' || ch == 'X') {
                if(table[guard.x][guard.y] == '.') {
                    visited++;
                }
                table[guard.x][guard.y] = 'X';
                guard = p;
            }
        }
        return visited;
    }


    private Pair<Point, Character> blockAhead(Point guard, Direction dir) {
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
            return Pair.with(new Point(x, y), table[x][y]);
        } else {
            return null;
        }
    }

    private Point findGuard() {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                char ch = table[i][j];
                if (ch == '^') {
                    return new Point(i, j);
                }
            }
        }
        throw new RuntimeException("can't find guard");
    }

    public void part2() {

    }

    private boolean checkCoords(int x, int y) {
        return x >= 0 && x < table.length && y >= 0 && y < table[x].length;
    }

    private void printTable() {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public Direction rotateRight() {
            return Direction.values()[(this.ordinal() + 1) % Direction.values().length];
        }
    }

    @AllArgsConstructor
    private static class Point {
        int x, y;
    }


}
