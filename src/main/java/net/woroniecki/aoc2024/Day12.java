package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day12 {

    @Override
    public String toString() {
        return "Day 12";
    }

    private final Plot[][] table;
    private final int h, w;

    private List<Region> regions = new ArrayList<>();

    public Day12(String input) {
        String[] lines = input.split("\n");
        table = new Plot[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            table[i] = new Plot[line.length()];
            for (int j = 0; j < line.length(); j++) {
                table[i][j] = new Plot(0, line.charAt(j), false);
            }
        }
        this.h = table.length;
        this.w = table[0].length;

        this.captureRegions();
        this.detectSides();
    }

    @AllArgsConstructor
    private static class Plot {
        int regionId;
        char ch;
        boolean visited = false;
    }

    @ToString
    @AllArgsConstructor
    private static class Point {

        int x, y;
    }


    @ToString
    public static class Region {
        static int nextId = 0;

        static int nextId() {
            return ++nextId;
        }


        char ch;
        int id = nextId();
        int area;
        int perimeter;
        int sides;
        int maxX, maxY, minX, minY;
    }

    public int part1() {
        captureRegions();
        return regions.stream().mapToInt(r -> r.area * r.perimeter).sum();
    }

    public int part2() {
        captureRegions();
        detectSides();
        return regions.stream().mapToInt(r -> r.area * r.sides).sum();
    }

    public Region region(char ch) {
        return regions.stream().filter(r -> r.ch == ch).findFirst().orElseThrow();
    }

    public void detectSides() {
        for (Region r : regions) {
            // check cols
            int segments = 0;
            for (int x = r.minX; x <= r.maxX; x++) {
                boolean aboveBlockStarted = false;
                boolean belowBlockStarted = false;
                for (int y = r.minY; y <= r.maxY; y++) {
                    boolean currentCheck = table[x][y].regionId == r.id;
                    boolean aboveCheck = x - 1 < 0 || table[x - 1][y].regionId != r.id;
                    boolean belowCheck = x + 1 >= h || table[x + 1][y].regionId != r.id;
                    if (currentCheck) {
                        if (aboveCheck && !aboveBlockStarted) {
                            aboveBlockStarted = true;
                            segments++;
                        }
                        if (belowCheck && !belowBlockStarted) {
                            belowBlockStarted = true;
                            segments++;
                        }
                    }

                    if (!currentCheck || !aboveCheck) {
                        aboveBlockStarted = false;
                    }

                    if (!currentCheck || !belowCheck) {
                        belowBlockStarted = false;
                    }
                }
            }

            // check rows
            for (int y = r.minY; y <= r.maxY; y++) {
                boolean leftBlockStarted = false;
                boolean rightBlockStarted = false;
                for (int x = r.minX; x <= r.maxX; x++) {
                    boolean currentCheck = table[x][y].regionId == r.id;
                    boolean leftCheck = y - 1 < 0 || table[x][y - 1].regionId != r.id;
                    boolean rightCheck = y + 1 >= w || table[x][y + 1].regionId != r.id;
                    if (currentCheck) {
                        if (leftCheck && !leftBlockStarted) {
                            leftBlockStarted = true;
                            segments++;
                        }
                        if (rightCheck && !rightBlockStarted) {
                            rightBlockStarted = true;
                            segments++;
                        }
                    }

                    if (!currentCheck || !leftCheck) {
                        leftBlockStarted = false;
                    }

                    if (!currentCheck || !rightCheck) {
                        rightBlockStarted = false;
                    }
                }
            }
            r.sides = segments;
        }
    }

    public void captureRegions() {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                Plot plot = table[i][j];
                // . means already visited and counted
                if (!plot.visited) {
                    Region region = detectRegion(i, j);
                    regions.add(region);
                }
            }
        }
    }

    private Region detectRegion(int x, int y) {

        Region region = new Region();
        LinkedList<Point> stack = new LinkedList<>();
        stack.push(new Point(x, y));

        int area = 0;
        int perimeter = 0;
        int maxX = 0, maxY = 0, minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        while (!stack.isEmpty()) {
            Point top = stack.pop();
            Plot plot = table[top.x][top.y];
            if (plot.visited) {
                continue;
            }
            region.ch = plot.ch;

            maxX = Math.max(maxX, top.x);
            maxY = Math.max(maxY, top.y);
            minX = Math.min(minX, top.x);
            minY = Math.min(minY, top.y);

            table[top.x][top.y].visited = true;
            table[top.x][top.y].regionId = region.id;
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
        region.maxX = maxX;
        region.maxY = maxY;
        region.minX = minX;
        region.minY = minY;
        region.area = area;
        region.perimeter = perimeter;
        return region;
    }


    private boolean checkCoords(int x, int y) {
        return x >= 0 && x < table.length && y >= 0 && y < table[x].length;
    }

}
