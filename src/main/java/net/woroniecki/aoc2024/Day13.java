package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day13 {


    @AllArgsConstructor
    private static class Equation {
        long x1, x2, x3;
        long y1, y2, y3;

        public void correctCoords() {
            this.x3 += 10000000000000L;
            this.y3 += 10000000000000L;
        }

        public Optional<Long> solveB() {
            long l = x1 * y3 - y1 * x3;
            long m = x1 * y2 - y1 * x2;
            if (l % m == 0) {
                return Optional.of(l / m);
            } else {
                return Optional.empty();
            }
        }

        public Optional<Long> solveA(long b) {
            long l = x3 - x2 * b;
            long m = x1;
            if (l % m == 0) {
                return Optional.of(l / m);
            } else {
                return Optional.empty();
            }
        }

        public Optional<Long> solve() {
            Optional<Long> b = this.solveB();
            Optional<Long> a = b.flatMap(this::solveA);

            if (a.isPresent()) {
                long price = a.get() * 3 + b.get();
                return Optional.of(price);
            } else {
                return Optional.empty();
            }
        }

    }

    private final List<Equation> equations = new ArrayList<>();

    public Day13(String input) {

        Pattern pattern = Pattern.compile("Button A: X\\+(?<x1>\\d+), Y\\+(?<y1>\\d+)(\\n|\\r){1,3}Button B: X\\+(?<x2>\\d+), Y\\+(?<y2>\\d+)(\\n|\\r){1,3}Prize: X=(?<x3>\\d+), Y=(?<y3>\\d+)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            long x1 = Long.parseLong(matcher.group("x1").trim());
            long x2 = Long.parseLong(matcher.group("x2").trim());
            long x3 = Long.parseLong(matcher.group("x3").trim());
            long y1 = Long.parseLong(matcher.group("y1").trim());
            long y2 = Long.parseLong(matcher.group("y2").trim());
            long y3 = Long.parseLong(matcher.group("y3").trim());

            equations.add(new Equation(x1, x2, x3, y1, y2, y3));
        }
    }

    public long part1() {
        return equations.stream()
                .map(Equation::solve)
                .filter(Optional::isPresent)
                .mapToLong(Optional::get)
                .sum();
    }

    public long part2() {
        return equations.stream()
                .peek(Equation::correctCoords)
                .map(Equation::solve)
                .filter(Optional::isPresent)
                .mapToLong(Optional::get)
                .sum();
    }

}
