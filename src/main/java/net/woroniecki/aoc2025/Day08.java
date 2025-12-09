package net.woroniecki.aoc2025;

import com.google.common.collect.Sets;
import groovy.lang.Tuple3;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.*;
import java.util.stream.IntStream;

public class Day08 {

    List<Point> points = new ArrayList<>();
    List<Set<Point>> circuits = new ArrayList<>();

    public Day08(String input) {
        String[] lines = input.trim().split("\n");
        int id = 0;
        for (String line : lines) {
            String[] coords = line.trim().split(",");
            int x = Integer.parseInt(coords[0].trim());
            int y = Integer.parseInt(coords[1].trim());
            int z = Integer.parseInt(coords[2].trim());
            Point p = new Point(id, x, y, z);
            points.add(p);
            circuits.add(Sets.newHashSet(p));
            id++;
        }
    }

    public int part1(int connections) {

        List<Tuple3<Point, Point, Long>> distances = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                Point p2 = points.get(j);
                long dist = p1.dist(p2);
                Tuple3<Point, Point, Long> tuple = Tuple3.tuple(p1, p2, dist);
                distances.add(tuple);
            }
        }

        distances.sort(Comparator.comparing(Tuple3::getV3));

        for (int d = 0; d < connections; d++) {
            Tuple3<Point, Point, Long> dist = distances.get(d);

            Point p1 = dist.getV1();
            Point p2 = dist.getV2();

            int i1 = IntStream.range(0, circuits.size()).filter($ -> circuits.get($).contains(p1)).findAny().getAsInt();
            int i2 = IntStream.range(0, circuits.size()).filter($ -> circuits.get($).contains(p2)).findAny().getAsInt();

            if (i1 != i2) {
                circuits.get(i1).addAll(circuits.get(i2));
                circuits.remove(i2);
            }
        }

        Comparator<Set<Point>> cmp = Comparator.comparing(Set::size);
        circuits.sort(cmp.reversed());
        Optional<Integer> result = circuits.stream()
                .sorted(cmp.reversed())
                .limit(3)
                .map(Set::size)
                .reduce((a, b) -> a * b);

        return result.get();
    }

    public long part2() {
        List<Tuple3<Point, Point, Long>> distances = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                Point p2 = points.get(j);
                long dist = p1.dist(p2);
                Tuple3<Point, Point, Long> tuple = Tuple3.tuple(p1, p2, dist);
                distances.add(tuple);
            }
        }

        distances.sort(Comparator.comparing(Tuple3::getV3));

        int d = 0;
        while (circuits.size() > 1) {
            Tuple3<Point, Point, Long> dist = distances.get(d);
            d++;

            Point p1 = dist.getV1();
            Point p2 = dist.getV2();

            int i1 = IntStream.range(0, circuits.size()).filter($ -> circuits.get($).contains(p1)).findAny().getAsInt();
            int i2 = IntStream.range(0, circuits.size()).filter($ -> circuits.get($).contains(p2)).findAny().getAsInt();

            if (i1 != i2) {
                circuits.get(i1).addAll(circuits.get(i2));
                circuits.remove(i2);
            }
        }

        Tuple3<Point, Point, Long> last = distances.get(d - 1);
        return (long) last.getV1().x * (long) last.getV2().x;
    }

    @Value
    @AllArgsConstructor
    static class Point {

        int id;
        int x, y, z;

        long dist(Point p) {
            long dx = (p.x - x);
            long dy = (p.y - y);
            long dz = (p.z - z);
            return dx * dx + dy * dy + dz * dz;
        }

    }

}
