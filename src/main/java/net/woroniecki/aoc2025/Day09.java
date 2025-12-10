package net.woroniecki.aoc2025;

import groovy.lang.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class Day09 {

    List<Tuple2<Long, Long>> coords = new ArrayList<>();

    public Day09(String input) {
        String[] lines = input.trim().split("\n");
        for (String line : lines) {
            String[] s = line.trim().split(",");
            long x = Long.parseLong(s[0]);
            long y = Long.parseLong(s[1]);
            coords.add(Tuple2.tuple(x, y));
        }
    }

    public long part1() {
        long max = 0;

        for (int i = 0; i < coords.size(); i++) {
            for (int j = 0; j < i; j++) {
                Tuple2<Long, Long> c1 = coords.get(i);
                Tuple2<Long, Long> c2 = coords.get(j);
                long a = Math.abs(c1.getV1() - c2.getV1()) + 1;
                long b = Math.abs(c1.getV2() - c2.getV2()) + 1;
                long area = a * b;
                max = Math.max(max, area);
            }
        }

        return max;
    }

    public int part2() {
        return 0;
    }


}
