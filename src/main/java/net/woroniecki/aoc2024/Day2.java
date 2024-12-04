package net.woroniecki.aoc2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {

    private final List<List<Integer>> reports;

    public Day2(String input) {
        reports = new ArrayList<>();
        for (String line : input.split("\n")) {
            List<Integer> report = Arrays.stream(line.split(" "))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            reports.add(report);
        }
    }

    public int safe1() throws Exception {
        return reports.stream()
                .mapToInt(r -> isSafe(r) ? 1 : 0)
                .sum();
    }

    public int safe2() throws Exception {
        return reports.stream()
                .mapToInt(r -> isSafe2(r) ? 1 : 0)
                .sum();
    }

    private boolean isSafe(List<Integer> report) {
        boolean increasing = report.get(0) < report.get(1);
        for (int i = 0; i < report.size() - 1; i++) {
            int a = report.get(i);
            int b = report.get(i + 1);
            int diff = (a - b);
            int diff2 = Math.abs(diff);
            if (diff2 == 0 || diff2 > 3) {
                return false;
            }

            if (increasing != diff < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isSafe2(List<Integer> report) {
        if (isSafe(report)) {
            return true;
        }
        for (int i = 0; i < report.size(); i++) {
            List<Integer> copy = new ArrayList<>(report);
            copy.remove(i);
            if (isSafe(copy)) {
                return true;
            }
        }
        return false;
    }


}
