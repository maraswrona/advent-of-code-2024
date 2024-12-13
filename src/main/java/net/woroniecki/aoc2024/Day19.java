package net.woroniecki.aoc2024;

import one.util.streamex.StreamEx;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Day19 {

    final List<String> towels;

    final Set<String> towelsSet;

    final List<String> designs;

    public Day19(String input) {
        String[] lines = input.split("\n");
        this.towels = Arrays.stream(lines[0].trim().split(", ")).toList();
        this.towelsSet = new HashSet<>(towels);

        this.designs = new ArrayList<>();
        for (int i = 2; i < lines.length; i++) {
            this.designs.add(lines[i].trim());
        }
    }

    public int part1() {
        String piped = StreamEx.of(towels).joining("|");
        String regex = "^(%s)+$".formatted(piped);
        Pattern pattern = Pattern.compile(regex);

        return (int) designs.stream()
                .map(design -> pattern.matcher(design).matches())
                .filter(t -> t)
                .count();
    }

    public int part2() {
        return 0;
    }

}
