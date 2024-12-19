package net.woroniecki.aoc2024;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Value;

public class Day10 {

    private final int[][] table;

    @Value
    @AllArgsConstructor
    static class Point {

        int x, y;
    }

    public Day10(String input) {
        String[] lines = input.split("\n");
        table = new int[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            char[] chars = lines[i].trim().toCharArray();
            table[i] = new int[chars.length];
            for (int j = 0; j < chars.length; j++) {
                table[i][j] = Integer.parseInt(String.valueOf(chars[j]));
            }
        }
    }

    public int part1() {
        return getTrailheads().stream()
                .mapToInt(p -> getPointsForTrailhead(p, true))
                .sum();
    }

    public int part2() {
        return getTrailheads().stream()
                .mapToInt(p -> getPointsForTrailhead(p, false))
                .sum();
    }

    public List<Point> getTrailheads() {
        List<Point> trailheads = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] == 0) {
                    trailheads.add(new Point(i, j));
                }
            }
        }
        return trailheads;
    }

    public int getPointsForTrailhead(Point trailhead, boolean distinct) {
        Set<Point> visited = new HashSet<>();
        LinkedList<Point> stack = new LinkedList<>();
        stack.push(trailhead);

        int points = 0;
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int h = table[p.x][p.y];
            if (h == 9) {
                points++;
                if (distinct) {
                    visited.add(p);
                }
                continue;
            }

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i * j == 0 && i != j) {
                        int x2 = p.x + i;
                        int y2 = p.y + j;
                        int h2 = getHeight(x2, y2);
                        Point p2 = new Point(x2, y2);
                        if (!visited.contains(p2) && h2 != -1 && h2 == h + 1) {
                            stack.push(p2);
                        }
                    }
                }
            }
        }

        return points;
    }

    public int getHeight(int x, int y) {
        if (checkCoords(x, y)) {
            return table[x][y];
        } else {
            return -1;
        }
    }

    private boolean checkCoords(int x, int y) {
        return x >= 0 && x < table.length && y >= 0 && y < table[x].length;
    }

}
