package net.woroniecki.aoc2024;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Day20 {

    public String toString() {
        return "";
    }

    private final Block[][] grid;

    @AllArgsConstructor
    @ToString
    public static class Block {

        int x, y;

        char ch;

        int number = -1;

        public boolean isTrack() {
            return ch == '.' || ch == 'E' || ch == 'S';
        }
    }

    public Day20(String input) {
        String[] lines = input.split("\n");
        grid = new Block[lines.length][];
        for (int y = 0; y < lines.length; y++) {
            char[] chars = lines[y].toCharArray();
            grid[y] = new Block[chars.length];
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = new Block(x, y, chars[x], -1);
            }
        }
    }

    public long part1() {
        System.out.println("Before");
        printGrid();
        Block current = findStart();
        int count = 0;
        current.number = count;
        log.info("Procesing block {}", current);
        while (current.ch != 'E') {
            current = findNext(current);
            current.number = ++count;
            log.info("Procesing block {}", current);
        }

        System.out.println("After");
        printGrid();

        allBlocks().forEach(b -> {
            if (b.ch != '#') return;

            if (checkCoords(b.x + 1, b.y) && checkCoords(b.x - 1, b.y)) {
                Block right = grid[b.y][b.x + 1];
                Block left = grid[b.y][b.x - 1];

                if (right.isTrack() && left.isTrack()) {
                    int number = Math.abs(right.number - left.number) - 2;
                    b.number = number;
                    log.info("Shortcut!: {}", b);
                }
            }

            if (checkCoords(b.x, b.y - 1) && checkCoords(b.x, b.y + 1)) {
                Block up = grid[b.y - 1][b.x];
                Block down = grid[b.y + 1][b.x];

                if (up.isTrack() && down.isTrack()) {
                    int number = Math.abs(up.number - down.number) - 2;
                    b.number = number;
                    log.info("Shortcut!: {}", b);
                }
            }
        });

        Map<Integer, Long> integerLongMap = StreamEx.of(allBlocks())
                .filter(b -> b.ch == '#')
                .groupingBy(b -> b.number, Collectors.counting());

        log.info("result: {}", integerLongMap);


        EntryStream.of(integerLongMap)
                        .sortedBy(Map.Entry::getKey)
                                .forEach(e-> log.info(e.toString()));

        long sum = EntryStream.of(integerLongMap)
                .filter(e -> e.getKey() >= 100)
                .mapToLong(Map.Entry::getValue)
                .sum();

        return sum;
    }

    private Block findNext(Block current) {
        int[][] dirs = new int[][]{ { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : dirs) {
            int x = dir[0] + current.x;
            int y = dir[1] + current.y;
            Block candidate = grid[y][x];
            if ((candidate.ch == '.' && candidate.number == -1) || candidate.ch == 'E') {
                return candidate;
            }
        }
        throw new RuntimeException("No candidate found");
    }

    public long part2() {
        System.out.println("Before");
        printGrid();
        List<Block> track = new ArrayList<>();
        Block current = findStart();
        track.add(current);
        int count = 0;
        current.number = count;
        log.info("Procesing block {}", current);
        while (current.ch != 'E') {
            current = findNext(current);
            current.number = ++count;
            track.add(current);
            log.info("Procesing block {}", current);
        }

        System.out.println("After");
        printGrid();

        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < track.size()-1; i++) {
            for (int j = i+1; j < track.size(); j++) {

                Block from = track.get(i);
                Block to = track.get(j);

                int dx = Math.abs(from.x - to.x);
                int dy = Math.abs(from.y - to.y);
                int jump = dx + dy;
                int save = to.number - from.number;

                int total = save - jump;

                if(jump <= 20) {
                    Integer curr = counts.getOrDefault(total, 0);
                    counts.put(total, curr + 1);
                }
            }
        }


        EntryStream.of(counts)
                .sortedBy(Map.Entry::getKey)
                .forEach(e-> log.info(e.toString()));

        long sum = EntryStream.of(counts)
                .filter(e -> e.getKey() >= 100)
                .mapToLong(Map.Entry::getValue)
                .sum();

        return sum;
    }

    public Block findStart() {
        return allBlocks().filter(b -> b.ch == 'S').findFirst().orElseThrow();
    }

    public Stream<Block> allBlocks() {
        return Arrays.stream(grid).flatMap(Arrays::stream);
    }

    public void printGrid() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x].ch == '.') {
                    System.out.print(grid[y][x].number);
                } else {
                    System.out.print(grid[y][x].ch);
                }
            }
            System.out.println();
        }
    }

    private boolean checkCoords(int x, int y) {
        return y >= 0 && y < grid.length && x >= 0 && x < grid[y].length;
    }

}
