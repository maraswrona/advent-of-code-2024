package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Day14 {

    @Getter
    final List<Robot> robots = new ArrayList<>();

    @Getter
    final int w, h;

    public Day14(String input, int w, int h) {
        this.w = w;
        this.h = h;

        Pattern pattern = Pattern.compile("p=(?<px>\\d+),(?<py>\\d+) v=(?<vx>-?\\d+),(?<vy>-?\\d+)([\\n\\r]){0,2}");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            int px = Integer.parseInt(matcher.group("px"));
            int py = Integer.parseInt(matcher.group("py"));
            int vx = Integer.parseInt(matcher.group("vx"));
            int vy = Integer.parseInt(matcher.group("vy"));

            PVector pos = new PVector(px, py);
            PVector vel = new PVector(vx, vy);
            Robot robot = new Robot(pos, vel);
            robots.add(robot);
        }
    }

    @AllArgsConstructor
    public class Robot {
        PVector p;
        PVector v;

        void update(int seconds) {
            p = p.add(v.mul(seconds)).mod(w, h);
        }
    }

    @AllArgsConstructor
    public static class PVector {
        final int x, y;

        PVector mul(int n) {
            return new PVector(x * n, y * n);
        }

        PVector add(PVector p) {
            return new PVector(x + p.x, y + p.y);
        }

        PVector mod(int w, int h) {
            return new PVector(Math.floorMod(x, w), Math.floorMod(y, h));
        }
    }

    private static enum Quadrant {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, NONE;

        static Quadrant fromLocation(PVector p, int w, int h) {
            if (p.x == w / 2 || p.y == h / 2) {
                return NONE;
            }

            boolean left = p.x < w / 2;
            boolean up = p.y < h / 2;
            if (left) {
                if (up) {
                    return TOP_LEFT;
                } else {
                    return BOTTOM_LEFT;
                }
            } else {
                if (up) {
                    return TOP_RIGHT;
                } else {
                    return BOTTOM_RIGHT;
                }
            }
        }
    }


    public int part1() {
        robots.forEach(r -> r.update(100));
        Map<Quadrant, List<Robot>> map = StreamEx.of(robots).groupingBy(r -> Quadrant.fromLocation(r.p, w, h));
        return EntryStream.of(map)
                .filterKeys(q -> q != Quadrant.NONE)
                .mapKeyValue((q, l) -> l.size())
                .reduce(1, (a, b) -> a * b);
    }

    public int part2() {
        return IntStreamEx.range(1, 10000)
                .mapToEntry(i -> i, i -> {
                    robots.forEach(r -> r.update(1));
                    IntSummaryStatistics stats = roboStats();
                    return stats.getMax();
                })
                .maxBy(Map.Entry::getValue)
                .get()
                .getKey();
    }

    private IntSummaryStatistics roboStats() {
        Map<Integer, List<Robot>> rows = StreamEx.of(robots).groupingBy(r -> r.p.y);

        Map<Integer, Integer> maxConsecutive = EntryStream.of(rows)
                .mapValues(list -> {
                    List<Robot> l = StreamEx.of(list).sortedBy(r -> r.p.x).toList();
                    int max = 1;
                    int counter = 1;
                    for (int i = 0; i < l.size() - 1; i++) {
                        Robot r1 = l.get(i);
                        Robot r2 = l.get(i + 1);
                        if (r2.p.x - r1.p.x == 1) {
                            counter++;
                            max = Math.max(max, counter);
                        } else {
                            counter = 1;
                        }
                    }
                    return max;
                }).toMap();

        return maxConsecutive.values().stream().mapToInt(Integer::intValue).summaryStatistics();
    }


}
