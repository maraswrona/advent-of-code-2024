package net.woroniecki.aoc2024;

import one.util.streamex.StreamEx;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 {

    final Set<String> towels;

    final List<String> designs;

    public Day19(String input) {
        String[] lines = input.split("\n");
        this.towels = Arrays.stream(lines[0].trim().split(", ")).collect(Collectors.toSet());

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

    public long part2() {
        Map<String, Long> cache = new HashMap<>();
        return designs.stream()
                .mapToLong(design -> countWaysToArrange(design, cache))
                .sum();
    }


    public long countWaysToArrange(String design, Map<String, Long> cache) {
        if (cache.containsKey(design)) {
            return cache.get(design);
        }

        long total = 0;
        for (int i = 1; i < design.length(); i++) {
            String prefix = design.substring(0, i);
            if (towels.contains(prefix)) {
                String suffix = design.substring(i);
                total += countWaysToArrange(suffix, cache);
            }
        }

        if (towels.contains(design)) {
            total += 1;
        }
        cache.put(design, total);
        return total;
    }


}
