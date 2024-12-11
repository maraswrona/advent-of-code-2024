package net.woroniecki.aoc2024;

import org.javatuples.Pair;

import java.util.*;

public class Day11 {

    private final List<Long> stones = new ArrayList<>();

    public Day11(String input) {
        for (String s : input.split(" ")) {
            stones.add(Long.parseLong(s));
        }
    }

    public long part1() {
        Map<Pair<Long, Long>, Long> results = new HashMap<>();
        return stones.stream().mapToLong(number -> blinkFast(number, 25L, results)).sum();
    }

    public long part2() {
        Map<Pair<Long, Long>, Long> results = new HashMap<>();
        return stones.stream().mapToLong(number -> blinkFast(number, 75L, results)).sum();
    }

    public long blinkFast(long number, long blinks, Map<Pair<Long, Long>, Long> results) {
        if (results.containsKey(Pair.with(number, blinks))) {
            return results.get(Pair.with(number, blinks));
        }

        if (blinks == 0) {
            return 1;
        }

        if (number == 0) {
            long res = blinkFast(1L, blinks - 1, results);
            results.put(Pair.with(1L, blinks - 1), res);
            return res;
        }

        if (String.valueOf(number).length() % 2 == 0) {
            String digits = String.valueOf(number);
            long left = Integer.parseInt(digits.substring(0, digits.length() / 2));
            long right = Integer.parseInt(digits.substring(digits.length() / 2));
            long leftRes = blinkFast(left, blinks - 1, results);
            results.put(Pair.with(left, blinks - 1), leftRes);
            long rightRes = blinkFast(right, blinks - 1, results);
            results.put(Pair.with(right, blinks - 1), rightRes);
            return leftRes + rightRes;
        }

        long res = blinkFast(number * 2024, blinks - 1, results);
        results.put(Pair.with(number * 2024, blinks - 1), res);
        return res;
    }

    public List<Long> blink(int blinks) {
        Map<Pair<Long, Long>, LinkedList<Long>> map = new HashMap<>();
        return stones.stream()
                .map(number -> blinkFull(number, blinks, map))
                .flatMap(Collection::stream)
                .toList();
    }

    public LinkedList<Long> blinkFull(long number, long blinks, Map<Pair<Long, Long>, LinkedList<Long>> results) {
        if (results.containsKey(Pair.with(number, blinks))) {
            return results.get(Pair.with(number, blinks));
        }

        if (blinks == 0) {
            LinkedList<Long> list = new LinkedList<>();
            list.add(number);
            return list;
        }

        if (number == 0) {
            LinkedList<Long> res = blinkFull(1L, blinks - 1L, results);
            results.put(Pair.with(1L, blinks - 1L), res);
            return res;
        }

        if (String.valueOf(number).length() % 2 == 0) {
            String digits = String.valueOf(number);
            long left = Integer.parseInt(digits.substring(0, digits.length() / 2));
            long right = Integer.parseInt(digits.substring(digits.length() / 2));
            LinkedList<Long> leftRes = blinkFull(left, blinks - 1, results);
            results.put(Pair.with(left, blinks - 1), leftRes);
            LinkedList<Long> rightRes = blinkFull(right, blinks - 1, results);
            results.put(Pair.with(right, blinks - 1), rightRes);
            LinkedList<Long> list = new LinkedList<>();
            list.addAll(leftRes);
            list.addAll(rightRes);
            return list;
        }

        LinkedList<Long> res = blinkFull(number * 2024, blinks - 1, results);
        results.put(Pair.with(number * 2024, blinks - 1), res);
        return res;
    }
}
