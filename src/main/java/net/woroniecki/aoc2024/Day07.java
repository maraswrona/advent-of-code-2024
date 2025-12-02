package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class Day07 {

    List<Case> cases = new ArrayList<>();

    @AllArgsConstructor
    static class Case {
        long result;
        List<Long> numbers;
    }

    public Day07(String input) {
        for (String line : input.split("\n")) {
            String[] parts = line.split(" ");
            Long result = Long.parseLong(parts[0].substring(0, parts[0].length() - 1).trim());
            List<Long> numbers = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                long value = Long.parseLong(parts[i].trim());
                numbers.add(value);
            }
            cases.add(new Case(result, numbers));
        }
    }

    public long part1() {
        return cases.stream()
                .filter(c -> checkOperators(c.result, c.numbers, false))
                .mapToLong(c -> c.result)
                .sum();
    }

    public long part2() {
        return cases.stream()
                .filter(c -> checkOperators(c.result, c.numbers, true))
                .mapToLong(c -> c.result)
                .sum();
    }

    private boolean checkOperators(long result, List<Long> numbers, boolean useConcat) {
        if (numbers.size() == 1) {
            return result == numbers.getFirst();
        }
        long last = numbers.getLast();
        List<Long> lastRemoved = numbers.subList(0, numbers.size() - 1);

        if (result % last == 0 && checkOperators(result / last, lastRemoved, useConcat)) {
            return true;
        }
        if (result > last && checkOperators(result - last, lastRemoved, useConcat)) {
            return true;
        }
        if (useConcat && result > last && endsWith(result, last) && checkOperators(removeSuffix(result, last), lastRemoved, useConcat)) {
            return true;
        }
        return false;
    }

    private long removeSuffix(long number, long suffix) {
        String str = String.valueOf(number);
        String search = String.valueOf(suffix);
        int index = str.lastIndexOf(search);
        return Long.parseLong(str.substring(0, index));
    }

    private boolean endsWith(long number, long suffix) {
        return String.valueOf(number).endsWith(String.valueOf(suffix));
    }
}
