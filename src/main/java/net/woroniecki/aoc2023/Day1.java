package net.woroniecki.aoc2023;

import one.util.streamex.StreamEx;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day1 {

    private final String input;
    private final Map<String, Integer> digits;

    public Day1(String input) {
        this.input = input;

        List<String> words = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

        digits = new HashMap<>();
        for (int d = 1; d <= 9; d++) {
            String word = words.get(d - 1);
            digits.put(word, d);
            digits.put(Integer.toString(d), d);
        }
    }

    public int part1() {
        int sum = 0;
        for (String line : input.split("\n")) {
            char[] chars = line.toCharArray();
            int left = -1, right = chars.length;
            while (!Character.isDigit(chars[++left])) ;
            while (!Character.isDigit(chars[--right])) ;
            sum += Integer.parseInt("" + chars[left] + chars[right]);

        }
        return sum;
    }

    public int part2() {
        int sum = 0;
        for (String line : input.split("\n")) {
            int first = firstDigit(line);
            int last = lastDigit(line);
            sum += Integer.parseInt("" + first + last);
        }
        return sum;
    }

    private int firstDigit(String line) {
        return StreamEx.of(digits.keySet())
                .map(d -> Pair.with(line.indexOf(d), d))
                .filter(p -> p.getValue0() != -1)
                .minBy(Pair::getValue0)
                .map(Pair::getValue1)
                .map(digits::get)
                .orElseThrow();
    }

    private int lastDigit(String line) {
        return StreamEx.of(digits.keySet())
                .map(d -> Pair.with(line.lastIndexOf(d), d))
                .filter(p -> p.getValue0() != -1)
                .maxBy(Pair::getValue0)
                .map(Pair::getValue1)
                .map(digits::get)
                .orElseThrow();
    }


}
