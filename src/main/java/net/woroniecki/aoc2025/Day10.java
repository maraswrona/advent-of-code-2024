package net.woroniecki.aoc2025;

import groovy.lang.Tuple2;
import groovy.lang.Tuple3;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.EntryStream;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Day10 {

    List<Case> cases = new ArrayList<>();

    public Day10(String input) {

        for (String line : input.split("\n")) {
            // [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}

            Pattern pattern = Pattern.compile("\\[(?<lights>[.#]+)] (?<buttons>.+)+ \\{(?<joltage>.+)}");

            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String lights = matcher.group("lights").trim();
                String buttonsTxt = matcher.group("buttons").trim();
                String joltagesTxt = matcher.group("joltage").trim();
                log.info("{} {} {}", lights, buttonsTxt, joltagesTxt);

                List<List<Byte>> buttons = new ArrayList<>();
                for (String $ : buttonsTxt.trim().split(" ")) {
                    List<Byte> button = new ArrayList<>();
                    for (String $$ : $.substring(1, $.length() - 1).split(",")) {
                        button.add(Byte.parseByte($$));
                    }
                    buttons.add(button);
                }

                List<Short> joltages = new ArrayList<>();
                for (String $ : joltagesTxt.split(",")) {
                    short jolt = Short.parseShort($);
                    joltages.add(jolt);
                }

                cases.add(new Case(lights, buttons, joltages));


            }
        }
    }

    public int part1() {
        return cases.stream()
                .map(Graph::new)
                .mapToInt(Graph::bfs)
                .sum();
    }

    public int part2() {
        return cases
                .subList(1, 2)
                .stream()
                .mapToInt(c -> bfs2(c.joltage, c.buttons))
                .sum();
    }

    @AllArgsConstructor
    private class Case {
        String lights;
        List<List<Byte>> buttons;
        List<Short> joltage;
    }

    private static class Graph {

        Map<Short, Set<Short>> connections;
        int target;

        Graph(Case testCase) {
            char[] charArray = testCase.lights.toCharArray();

            for (int i = 0; i < charArray.length; i++) {
                char ch = charArray[i];
                int bit = 1 << i;
                if (ch == '#') {
                    target |= bit;
                }
            }

            int bits = testCase.lights.length();
            int nodes = 1 << bits;
            connections = new HashMap<>(nodes);

            for (short i = 0; i < nodes; i++) {
                connections.put(i, new HashSet<>());
            }

            for (List<Byte> positions : testCase.buttons) {
                for (short a = 0; a < nodes; a++) {
                    short b = a;
                    for (byte position : positions) {
                        b = (short) (b ^ (1 << position));
                    }
                    this.connect(a, b);
                }
            }

        }

        void connect(short a, short b) {
            connections.get(a).add(b);
            connections.get(b).add(a);
        }

        int bfs() {

            Deque<Short> queue = new ArrayDeque<>();
            queue.addLast((short) 0);

            Set<Short> visited = new HashSet<>();
            int steps = 0;
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    Short node = queue.removeFirst();
                    visited.add(node);

                    if (node == target) {
                        return steps;
                    }

                    for (short out : connections.get(node)) {
                        if (!visited.contains(out)) {
                            queue.addLast(out);
                        }
                    }
                }
                steps++;
            }
            throw new IllegalStateException("Cannot reach destination");
        }
    }

    int bfs2(List<Short> target, List<List<Byte>> buttons) {

        log.info("target: {} buttons: {}", target, buttons);

        Deque<List<Short>> queue = new LinkedList<>();
        queue.addLast(target);

        int steps = 0;
        Set<List<Short>> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            log.info("step {}, visited {} queue size {} peek: {}", steps, visited.size(), queue.size(), queue.peek());

            int size = queue.size();
            for (int i = 0; i < size; i++) {
                if(i % 100000 == 0) {
                    log.info("progress {}%", (float)i / (float)size * 100);
                }
                List<Short> counters = queue.removeFirst();
                visited.add(counters);

                boolean allZeros = counters.stream().allMatch(v -> v == 0);
                if (allZeros) {
                    return steps;
                }

                buttons = sortButtons(buttons, counters);

                for (List<Byte> button : buttons) {
                    ArrayList<Short> newCounters = new ArrayList<>(counters);
                    for (byte idx : button) {
                        newCounters.set(idx, (short) (newCounters.get(idx) - 1));
                    }
                    boolean noNegatives = newCounters.stream().allMatch(v -> v >= 0);
                    if (noNegatives && !visited.contains(newCounters)) {
                        queue.addLast(newCounters);
                        visited.add(newCounters);
                    }
                }
            }
            steps++;
        }
        throw new IllegalStateException("Cannot reach destination");
    }

    List<List<Byte>> sortButtons(List<List<Byte>> buttons, List<Short> counters) {

        Supplier<Short> scorer = new Supplier<>() {
            short start = 1;

            @Override
            public Short get() {
                short res = start;
                start = (short) (start * 2);
                return res;
            }
        };

        // create score lookup structure

        Map<Byte, Tuple3<Byte, Short, Short>> scoreLookup = EntryStream.of(counters)
                .map(e -> Tuple2.tuple((byte) e.getKey().intValue(), e.getValue()))
                .sorted(Comparator.<Tuple2<Byte, Short>, Short>comparing(Tuple2::getV2))
                .map(t -> Tuple3.tuple(t.getV1(), t.getV2(), scorer.get()))
                .toMap(Tuple3::getV1, Function.identity());
        // v1 = index, original position in counters (represents button id)
        // v2 = counter - we first sort by this, to prefer higher counters first
        // v3 = score - we assign growing, then create a map from index to score

        // assign score to each button combination


        List<Tuple3<Integer, List<Byte>, Integer>> list = EntryStream.of(buttons)
                .map(e -> {
                    Integer position = e.getKey();// position
                    List<Byte> btns = e.getValue();
                    int score = btns.stream().mapToInt(btn -> scoreLookup.get(btn).getV3()).sum();
                    return Tuple3.tuple(position, btns, score);
                })
                .sorted(Comparator.<Tuple3<Integer, List<Byte>, Integer>, Integer>comparing(Tuple3::getV3).reversed())
                .toList();


        return list.stream().map(Tuple3::getV2).toList();


    }


}
