package net.woroniecki.aoc2025;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import groovy.lang.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

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
        return graph.dfs(from, to);
    }

    public long part2() {
        graph.part2();

        return 0;
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

        public long dfs(String from, String to) {
//            log.info("\n ======= DFS ========= \n");

            IntSummaryStatistics stackStat = new IntSummaryStatistics();

            Deque<String> stack = new ArrayDeque<>();
            stack.push(from);

            int c = 0;
            long paths = 0;
            while (!stack.isEmpty()) {
                stackStat.accept(stack.size());

                if (c++ % 10000 == 0) {
                    log.info("stack size: avg: {} max: {} curr: {}", stackStat.getAverage(), stackStat.getMax(), stack.size());
                }

                String curr = stack.pop();

                if (curr.equals(to)) {
                    paths++;
                    continue;
                }

                Set<String> outputs = graphOut.get(curr);
                stack.addAll(outputs);
            }

            log.info("stack size: avg: {} max: {} curr: {}", stackStat.getAverage(), stackStat.getMax(), stack.size());
            return paths;
        }

        public long dfsBack(String from, Set<String> to, String mustContain) {
            //            log.info("\n ======= DFS ========= \n");

            IntSummaryStatistics stackStat = new IntSummaryStatistics();

            Deque<List<String>> stack = new ArrayDeque<>();
            stack.push(Lists.newArrayList(from));

            int c = 0;
            long paths = 0;
            while (!stack.isEmpty()) {
                stackStat.accept(stack.size());

                if (c++ % 100000 == 0) {
//                    log.info("stack size: avg: {} max: {} curr: {}", stackStat.getAverage(), stackStat.getMax(), stack.size());
                }

                List<String> path = stack.pop();

                if (to.contains(path.getLast())) {
                    if (mustContain == null || path.contains(mustContain)) {
                        paths++;
                    }
                    continue;
                }

                Set<String> outputs = graphOut.get(path.getLast());
                for (String output : outputs) {
                    ArrayList<String> newPath = Lists.newArrayList(path);
                    newPath.add(output);
                    stack.add(newPath);
                }
            }

            log.info("stack size: avg: {} max: {} curr: {}", stackStat.getAverage(), stackStat.getMax(), stack.size());
            return paths;
        }

        public void part2() {

            List<Tuple2<List<String>, String>> levels = List.of(
                    Tuple2.tuple(List.of("svr"), null),
                    Tuple2.tuple(List.of("voy", "qxn", "jjm"), "fft"),
                    Tuple2.tuple(List.of("wgs", "zrf", "fta", "caz", "vyd"), null),
                    Tuple2.tuple(List.of("gep", "bhg", "jxx", "qdw"), null),
                    Tuple2.tuple(List.of("zet", "fhn", "rnz", "qma", "qxk"), "dac"),
                    Tuple2.tuple(List.of("you", "itr", "txy"), null),
                    Tuple2.tuple(List.of("out"), null)
            );

            long product = 1;
            for (int i = 1; i < levels.size(); i++) {
                List<String> from = levels.get(i - 1).getV1();
                String via = levels.get(i - 1).getV2();
                List<String> to = levels.get(i).getV1();
                log.info("calculating all paths from {} to {} via {}", from, to, via);

                long sum = 0;
                for (String f : from) {
                    log.info("calculating all paths from {} to {} via {}", f, to, via);
                    long d = dfsBack(f, new HashSet<>(to), via);
                    sum += d;
                    log.info("paths from {} to {} ({})", f, to, d);
                }

                product *= sum;
                log.info("sum for all paths from {} to {}: ({})", from, to, sum);
                log.info("product so far: {}", product);
            }
        }

        public void dp() {

            Map<String, Long[]> cost = new HashMap<>();
            cost.put("svr", new Long[]{1L, 0L, 0L, 0L});

            Deque<String> queue = new ArrayDeque<>();
            queue.addAll(graphOut.keySet());

            while (!queue.isEmpty()) {
                String node = queue.removeFirst();
                Set<String> inputs = graphIn.get(node);
                if (cost.keySet().containsAll(inputs)) {
                    Long[] r = new Long[]{0L, 0L, 0L, 0L};

                    for (String in : inputs) {
                        Long[] prev = cost.get(in);

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
                log.info("Queue size: {}", queue.size());
            }

            log.info("cost: {}", cost);
            log.info("out {}", Arrays.toString(cost.get("out")));
        }


        static class State {
            String node;
            List<String> path;

            State(String node, List<String> path) {
                this.node = node;
                this.path = path;
            }
        }

        public static List<List<String>> findAllPaths(
                Map<String, Set<String>> graph,
                String start,
                String end
        ) {
            List<List<String>> result = new ArrayList<>();
            Deque<State> stack = new ArrayDeque<>();

            // initialize DFS
            stack.push(new State(start, List.of(start)));

            while (!stack.isEmpty()) {
                State current = stack.pop();
                String node = current.node;
                List<String> path = current.path;

                // reached destination
                if (node.equals(end)) {
                    result.add(path);
                    continue;
                }

                // explore neighbors
                for (String neighbor : graph.getOrDefault(node, Set.of())) {
                    // avoid cycles in the current path
                    if (!path.contains(neighbor)) {
                        List<String> newPath = new ArrayList<>(path);
                        newPath.add(neighbor);
                        stack.push(new State(neighbor, newPath));
                    }
                }
            }

            return result;
        }


        public long dfs2(String from, String to) {

            log.info("\n ======= DFS 2 ========= \n");

            LongSummaryStatistics stackStat = new LongSummaryStatistics();

            Deque<Node> stack = new ArrayDeque<>();
            stack.push(new Node(from, graphOut.get(from)));

            Set<String> visited = new HashSet<>();

            long c = 0;
            long paths = 0;
            while (!stack.isEmpty()) {
                stackStat.accept(stack.size());

                if (c++ % 1000000 == 0) {
                    log.info("paths: {} stack size: avg: {} max: {} curr: {}", paths, stackStat.getAverage(), stackStat.getMax(), stack.size());
                }

                Node curr = stack.element();

                if (curr.name.equals(to)
//                        && curr.getV2() && curr.getV3()
                ) {
                    paths++;
                    Node popped = stack.pop();
//                    visited.add(popped.name);
                    continue;
                }

//                boolean dac = curr.getV1().equals("dac") || curr.getV2();
//                boolean fft = curr.getV1().equals("fft") || curr.getV3();

                boolean wentForward = false;
                for (String output : curr.outputs.keySet()) {
                    if (curr.outputs.get(output) == false && !visited.contains(output)) {
                        curr.outputs.put(output, true);
                        stack.push(new Node(output, graphOut.get(output)));
                        wentForward = true;

                        if (visited.contains(output)) {
                            log.info("visited: {} ", output);
                        }

                        break;
                    }
                }

                if (!wentForward) {
                    Node popped = stack.pop();
                    visited.add(popped.name);
                }

            }

            log.info("stack size: avg: {} max: {} curr: {}", stackStat.getAverage(), stackStat.getMax(), stack.size());
            return paths;
        }

        public long dfs3(String start) {
            Path path = new Path(graphOut);

            path.start("svr");

            long paths = 0;
            long matching = 0;
            while (path.hasOptionsToGo()) {
                while (!path.reachedEnd("fft")) {
                    path.takeNextAvailableStep();
                }
                paths++;
                if (paths % 500000 == 0) {
                    log.info("paths: {} last: {}", paths, path);
                }
//                if (path.contains("dac", "fft")) {
//                    matching++;
//                    log.info("FOUND MATCHING");
//                }
                path.goBackToLastAvailable();
            }
            return matching;

        }

        void analyzeGraphStructure() {

            Deque<String> queue = new ArrayDeque<>();
            queue.addLast("svr");

            Set<String> visited = new HashSet<>();
            Set<String> level = new HashSet<>();

            level.add("svr");
            int l = 0;
            while (!level.isEmpty()) {
                log.info("current level: {} - nodes: {}", l++, level);
                Sets.SetView<String> currentLevelAreadySeen = Sets.intersection(level, visited);

                log.info("nodes already seen: {} - {}", currentLevelAreadySeen.size(), currentLevelAreadySeen);

                Set<String> nextLevel = new HashSet<>();

                for (String node : level) {
                    nextLevel.addAll(graphOut.get(node));
                }

                visited.addAll(level);
                level = nextLevel;
            }

        }


        static class Node {
            String name;
            Map<String, Boolean> outputs;

            public Node(String name, Set<String> outputs) {
                this.name = name;
                this.outputs = new HashMap<>(outputs.size());
                for (String output : outputs) {
                    this.outputs.put(output, false);
                }
            }

            void visit(String output) {
                this.outputs.put(output, true);
            }

            Optional<String> nextAvailableOutput() {
                return EntryStream.of(outputs)
                        .findFirst(e -> e.getValue() == false)
                        .map(Map.Entry::getKey);
            }

        }

        @RequiredArgsConstructor
        static class Path {

            final Map<String, Set<String>> graph;
            final List<Node> steps = new ArrayList<>();

            void start(String start) {
                steps.add(new Node(start, graph.get(start)));
            }

            boolean hasOptionsToGo() {
                return steps.getLast().nextAvailableOutput().isPresent();
            }

            void takeNextAvailableStep() {
                Node last = steps.getLast();

                Optional<String> next = last.nextAvailableOutput();
                if (next.isPresent()) {
                    last.visit(next.get());
                    steps.add(new Node(next.get(), graph.get(next.get())));
                } else {
                    steps.removeLast();
                }
            }

            void goBackToLastAvailable() {
                while (steps.getLast().nextAvailableOutput().isEmpty()) {
                    steps.removeLast();
                }
            }

            boolean reachedEnd(String end) {
                return steps.getLast().name.equals(end);
            }

            boolean contains(String... labels) {
                Set<String> set = Set.of(labels);
                Set<String> path = StreamEx.of(steps).map(s -> s.name).toSet();
                return path.containsAll(set);
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                for (Node step : steps) {
                    sb.append(step.name).append(",");
                }

                return sb.toString();
            }

        }
    }

}
