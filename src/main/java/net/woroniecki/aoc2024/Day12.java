package net.woroniecki.aoc2024;

import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;

import lombok.AllArgsConstructor;
import lombok.ToString;

public class Day12 {

    private final Plot[][] table;

    public Day12(String input) {
        String[] lines = input.split("\n");
        table = new Plot[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            table[i] = new Plot[line.length()];
            for (int j = 0; j < line.length(); j++) {
                table[i][j] = new Plot(line.charAt(j), false);
            }

        }
    }

    @AllArgsConstructor
    private static class Plot {
        char ch;
        boolean visited = false;
    }

    @ToString
    @AllArgsConstructor
    private static class Point {

        int x, y;
    }

    public int part1() {
        int total = 0;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                Plot plot = table[i][j];
                // . means already visited and counted
                if (!plot.visited) {
                    int cost = measureRegion(i, j);
                    total += cost;
                }
            }
        }
        return total;
    }

    private int measureRegion(int x, int y) {

        LinkedList<Point> stack = new LinkedList<>();
        stack.push(new Point(x, y));

        int area = 0;
        int perimeter = 0;
        while (!stack.isEmpty()) {
            Point top = stack.pop();
            Plot plot = table[top.x][top.y];
            if (plot.visited) {
                continue;
            }

            table[top.x][top.y].visited = true;
            area++;

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i * j == 0 && i != j) {
                        int nx = top.x + i;
                        int ny = top.y + j;
                        if (checkCoords(nx, ny) && table[nx][ny].ch == plot.ch) {
                            stack.push(new Point(nx, ny));
                        } else {
                            perimeter++;
                        }
                    }
                }
            }
        }
        return area * perimeter;
    }

    public int part2() {
        return 0;
    }

    private boolean checkCoords(int x, int y) {
        return x >= 0 && x < table.length && y >= 0 && y < table[x].length;
    }

}
