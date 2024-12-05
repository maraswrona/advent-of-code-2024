package net.woroniecki.aoc2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day5 {

    private final Set<String> rules = new HashSet<>();
    private final List<List<Integer>> lists = new ArrayList<>();

    public Day5(String input) {

        for (String line : input.split("\n")) {
            if (line.trim().contains("|")) {
                rules.add(line.trim());
                continue;
            }

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.trim().split(",");
            List<Integer> list = new ArrayList<>();
            for (String part : parts) {
                int i = Integer.parseInt(part.trim());
                list.add(i);
            }
            lists.add(list);
        }
    }

    public int part1() {
        int sum = 0;
        for (List<Integer> list : lists) {
            boolean correct = false;
            for (int i = 0; i < list.size() - 1; i++) {
                int a = list.get(i);
                int b = list.get(i + 1);
                String rule = a + "|" + b;
                if(rules.contains(rule)) {
                    correct = true;
                } else {
                    correct = false;
                    break;
                }
            }
            if(correct) {
                int middle = list.get(list.size() / 2);
                sum += middle;
            }
        }
        return sum;
    }



    public void part2() {

    }


}