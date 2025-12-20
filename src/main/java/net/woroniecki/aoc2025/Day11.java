package net.woroniecki.aoc2025;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Day11 {

    Graph graph = new Graph();

    public Day11(String input) {

        input.lines().forEach(line -> {
            String[] parts = line.trim().split(": ");
            String from = parts[0];
            for (String to : parts[1].split(" ")) {
                graph.connect(from.trim(), to.trim());
            }
        });
    }

    public long part1(String from, String to) {
        long[] dp = graph.dp(from, to);
        return dp[0] + dp[1] + dp[2] + dp[3];
    }

    public long part2(String from, String to) {
        return graph.dp(from, to)[3];
    }

    public static class Graph {

        Map<String, Set<String>> graphOut = new HashMap<>();
        Map<String, Set<String>> graphIn = new HashMap<>();

        public void connect(String from, String to) {
            graphOut.putIfAbsent(from, new HashSet<>());
            graphOut.putIfAbsent(to, new HashSet<>());
            graphOut.get(from).add(to);

            graphIn.putIfAbsent(from, new HashSet<>());
            graphIn.putIfAbsent(to, new HashSet<>());
            graphIn.get(to).add(from);
        }

        public long[] dp(String from, String to) {

            Map<String, long[]> cost = new LinkedHashMap<>();
            cost.put(from, new long[]{1L, 0L, 0L, 0L});

            Deque<String> queue = new ArrayDeque<>();
            queue.addAll(graphOut.keySet());
            queue.remove(from);

            while (!queue.isEmpty()) {
                String node = queue.removeFirst();
                Set<String> inputs = graphIn.get(node);
                if (cost.keySet().containsAll(inputs)) {
                    long[] r = new long[]{0L, 0L, 0L, 0L};

                    for (String in : inputs) {
                        long[] prev = cost.get(in);

                        for (int mask = 0; mask < 4; mask++) {
                            long ways = prev[mask];
                            if (ways == 0) continue;

                            int newMask = mask;

                            if (node.equals("fft")) newMask |= 1;
                            if (node.equals("dac")) newMask |= 2;

                            r[newMask] += ways;
                        }
                    }

                    cost.put(node, r);
                } else {
                    queue.addLast(node);
                }
            }
            return cost.get(to);
        }
    }
}
