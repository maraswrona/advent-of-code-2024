package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;
import lombok.Value;
import one.util.streamex.StreamEx;

import java.util.*;

public class Day08 {

    final List<Antenna> antennas = new ArrayList<>();
    final int width, height;
    final Set<Point> antinodes = new HashSet<>();

    @AllArgsConstructor
    static class Antenna {
        final Point p;
        final char f;
    }

    @AllArgsConstructor
    @Value
    static class Point {
        int x, y;

        Point plus(Point p) {
            return new Point(x + p.x, y + p.y);
        }

        Point minus(Point p) {
            return new Point(x - p.x, y - p.y);
        }

        Point negate() {
            return new Point(-x, -y);
        }
    }

    public Day08(String input) {
        String[] split = input.split("\n");
        height = split.length;
        width = split[0].trim().length();
        for (int i = 0; i < split.length; i++) {
            String line = split[i].trim();
            char[] chars = line.trim().toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                if (c != '.') {
                    antennas.add(new Antenna(new Point(i, j), c));
                }
            }
        }
    }

    public int part1() {
        Map<Character, List<Antenna>> antennasPerFreq = StreamEx.of(antennas).groupingBy(a -> a.f);

        antennasPerFreq.forEach((freq, ants) -> {
            for (int i = 0; i < ants.size() - 1; i++) {
                for (int j = i + 1; j < ants.size(); j++) {
                    Antenna a1 = ants.get(i);
                    Antenna a2 = ants.get(j);

                    Point diff = a1.p.minus(a2.p);

                    Point antinode1 = a1.p.plus(diff);
                    Point antinode2 = a2.p.plus(diff.negate());

                    if (checkCoords(antinode1)) {
                        antinodes.add(antinode1);
                    }

                    if (checkCoords(antinode2)) {
                        antinodes.add(antinode2);
                    }
                }
            }
        });

        return antinodes.size();

    }

    public int part2() {
        Map<Character, List<Antenna>> antennasPerFreq = StreamEx.of(antennas).groupingBy(a -> a.f);

        antennasPerFreq.forEach((freq, ants) -> {
            for (int i = 0; i < ants.size() - 1; i++) {
                for (int j = i + 1; j < ants.size(); j++) {
                    Point a1 = ants.get(i).p;
                    Point a2 = ants.get(j).p;
                    Point diff = a1.minus(a2);

                    while (checkCoords(a1)) {
                        antinodes.add(a1);
                        a1 = a1.plus(diff);
                    }

                    while (checkCoords(a2)) {
                        antinodes.add(a2);
                        a2 = a2.plus(diff.negate());
                    }
                }
            }
        });

        return antinodes.size();
    }

    private boolean checkCoords(Point p) {
        return p.x >= 0 && p.x < height && p.y >= 0 && p.y < width;
    }


}
