package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@ToString
public class Day20 {

    @ToString.Exclude
    private final Block[][] grid;
    @ToString.Exclude
    private final List<Block> track;

    @ToString
    @AllArgsConstructor
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

        this.track = markTrack();
    }

    public long part1() {
        return findCheats(2, 100);
    }

    public long part2() {
        return findCheats(20, 100);
    }

    public long findCheats(int maxJump, int minSave) {
        long count = 0;

        for (int i = 0; i < track.size() - 1; i++) {
            for (int j = i + 1; j < track.size(); j++) {

                Block from = track.get(i);
                Block to = track.get(j);

                int dx = Math.abs(from.x - to.x);
                int dy = Math.abs(from.y - to.y);
                int jump = dx + dy;
                int save = to.number - from.number;

                int total = save - jump;

                if (jump <= maxJump && total >= minSave) {
                    count++;
                }
            }
        }

        return count;
    }

    private List<Block> markTrack() {
        List<Block> track = new ArrayList<>();
        Block current = findStart();
        track.add(current);
        int count = 0;
        current.number = count;
        while (current.ch != 'E') {
            current = findNext(current);
            current.number = ++count;
            track.add(current);
        }
        return track;
    }

    private Block findNext(Block current) {
        int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : dirs) {
            int x = dir[0] + current.x;
            int y = dir[1] + current.y;
            Block candidate = grid[y][x];
            if (candidate.isTrack() && candidate.number == -1) {
                return candidate;
            }
        }
        throw new RuntimeException("No candidate found");
    }

    private Block findStart() {
        return allBlocks().filter(b -> b.ch == 'S').findFirst().orElseThrow();
    }

    private Stream<Block> allBlocks() {
        return Arrays.stream(grid).flatMap(Arrays::stream);
    }

    private boolean checkCoords(int x, int y) {
        return y >= 0 && y < grid.length && x >= 0 && x < grid[y].length;
    }

}
