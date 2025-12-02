package net.woroniecki.aoc2024;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day01 {

    private final List<Integer> list1 = new ArrayList<>();
    private final List<Integer> list2 = new ArrayList<>();

    public Day01(String input) {
        for (String line : input.split("\n")) {
            String[] parts = line.split(" {3}");
            String first = parts[0].trim();
            String second = parts[1].trim();

            Integer int1 = Integer.valueOf(first);
            Integer int2 = Integer.valueOf(second);

            list1.add(int1);
            list2.add(int2);
        }
    }

    public int distance() {
        Collections.sort(list1);
        Collections.sort(list2);

        int sum = 0;
        for (int i = 0; i < list1.size(); i++) {
            Integer left = list1.get(i);
            Integer right = list2.get(i);
            sum += Math.abs(left - right);
        }
        return sum;
    }

    public int similarity() {
        Multiset<Integer> counts = HashMultiset.create(list2);
        return list1.stream()
                .mapToInt(i -> i * counts.count(i))
                .sum();
    }
}
