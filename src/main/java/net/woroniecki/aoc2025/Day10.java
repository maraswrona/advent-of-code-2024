package net.woroniecki.aoc2025;

import groovy.lang.Tuple2;
import groovy.lang.Tuple3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
                .stream()
                .mapToInt(c -> bfs3(c.joltage, c.buttons))
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
                if (i % 100000 == 0) {
                    log.info("progress {}%", (float) i / (float) size * 100);
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

    int bfs3(List<Short> t, List<List<Byte>> buttons) {

        List<Mask> masks = buttons.stream()
                .map(btns -> new Mask(btns, (byte) t.size()))
                .toList();

        Target target = new Target(t);

        log.info("target: {} ", target);

        long totalCombinations = 1;
        for (Mask mask : masks) {
            log.info("mask: {}", mask);
            mask.maxMult(target);
            totalCombinations *= mask.mult;
            mask.mult = 0;
        }

        log.info("Total combinations: {}", totalCombinations);

        long i = 0;
        int maskIdx = 0;
        int best = t.stream().mapToInt($ -> $).sum();
        do {
            i++;

            if (i % 5000000 == 0) {
                String progress = String.format("%d/%d %f", i, totalCombinations, (double) i / (double) totalCombinations * 100);
                log.info("progress {} - target: {} mults: {} best: {}", progress, target, masks.stream().map(m -> m.mult).toList(), best);
            }
            if (maskIdx >= masks.size()) {
                do {
                    maskIdx--;
                } while (masks.get(maskIdx).mult == 0);
                Mask mask = masks.get(maskIdx);
                mask.mult--;
                target.addOnce(mask);
                maskIdx++;
            } else {
                Mask mask = masks.get(maskIdx);
                mask.maxMult(target);
                target.subtract(mask);
                maskIdx++;
            }

            if (target.isZero()) {
                log.info("found potential solution! - target: {} mults: {} best: {}", target, masks.stream().map(m -> m.mult).toList(), best);
                int solution = masks.stream().mapToInt(m -> m.mult).sum();
                best = Math.min(best, solution);
            }

        } while (masks.stream().anyMatch(m -> m.mult != 0));

        return best;
    }

    @ToString
    static class Target {

        int[] target;

        Target(List<Short> target) {
            this.target = new int[target.size()];
            for (int i = 0; i < target.size(); i++) {
                Short s = target.get(i);
                this.target[i] = s;
            }
        }

        public void subtract(Mask mask) {
            for (int i = 0; i < this.target.length; i++) {
                target[i] -= mask.mask[i] * mask.mult;
            }
        }


        public void addOnce(Mask mask) {
            for (int i = 0; i < this.target.length; i++) {
                target[i] += mask.mask[i];
            }
        }

        public boolean isZero() {
            return Arrays.stream(target).allMatch(a -> a == 0);
        }

    }

    @ToString
    static class Mask {
        static char NEXT_CHAR = 'A';
        char id;
        int[] mask;
        int mult = 0;

        Mask(List<Byte> buttons, byte totalButtons) {
            id = NEXT_CHAR;
            NEXT_CHAR++;
            mask = new int[totalButtons];
            for (Byte button : buttons) {
                mask[button.intValue()] = 1;
            }
        }

        public void maxMult(Target target) {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < mask.length; i++) {
                if (mask[i] == 1) {
                    min = Math.min(target.target[i], min);
                }
            }
            mult = min;
        }

        public boolean containsTarget(int idx) {
            return mask[idx] == 1;
        }
    }

    int bfs4(List<Short> t, List<List<Byte>> buttons) {

        List<Mask> masks = buttons.stream()
                .map(btns -> new Mask(btns, (byte) t.size()))
                .toList();

        Target target = new Target(t);

        log.info("target: {} ", target);


        for (Mask mask : masks) {
            log.info("mask: {}", mask);
            mask.maxMult(target);
            mask.mult = 0;
        }

        int[] ints = target.target;
        List<MaskSet> maskSets = new ArrayList<>();
        for (int i = 0; i < ints.length; i++) {
            int trg = ints[i];
            int finalI = i;
            Set<Mask> set = masks.stream().filter(m -> m.containsTarget(finalI)).collect(Collectors.toSet());
            maskSets.add(new MaskSet(set, trg));
        }
        return 0;
    }

    @AllArgsConstructor
    static class MaskSet {
        Set<Mask> set = new HashSet<>();
        int target;
    }

    public static class Solver {


        public List<Solution> solve(Equations equations) {

            // 1. find equation with lowest number of vars - if tie take the one with lowest sum
            // 2. take first var from that equation, iterate over 0 - sum and assign values to that var
            // each type resolve and substitute the rest of equations where possible, simplifying where possible
            // keep track of assigned value and equations state (stack, state copy)
            // continue deeper with steps 1. 2. by assigning further values to next variables
            // when all equations are solved and all vars have values - we have one of possible solutions
            // save the solution (map of var -> value) and go back to stack, restore previous state, and try another value for the last var
            // continue until the stack is empty and all the vars have been tried with all possible values
            // at the end return all solutions
            // * if many solutions we can simplify and just track the sum of variables

            Deque<Equations> queue = new ArrayDeque<>();
            queue.addLast(equations);

            List<Solution> solutions = new ArrayList<>();

            while (!queue.isEmpty()) {
                Equations eqs = queue.removeFirst();
                if (eqs.isSolved()) {
                    solutions.add(new Solution(eqs.getVars()));
                } else {
                    Equation eq = eqs.findSmallest();
                    char variable = eq.getVars().stream().findFirst().get();
                    int sum = eq.getSum();

                    for (int value = 0; value <= sum; value++) {
                        Equations copy = eqs.copy();
                        copy.apply(variable, value);
                        queue.addLast(copy);
                    }
                }
            }

            return solutions;
        }

    }

    @Getter
    public static class Equations {
        Map<Character, Integer> vars = new HashMap<>();
        List<Equation> equations;

        public Equations(List<Equation> equations) {
            this.equations = equations;
        }

        public Equations(Map<Character, Integer> vars, List<Equation> equations) {
            this.vars = vars;
            this.equations = equations;
        }

        public Equation findSmallest() {
            Comparator<Equation> cmp = Comparator
                    .<Equation, Integer>comparing(e -> e.getVars().size())
                    .thenComparing(Equation::getSum);
            return StreamEx.of(equations).min(cmp).get();
        }

        public boolean isSolved() {
            return equations.isEmpty();
        }

        public Equations copy() {
            ArrayList<Equation> eqs = new ArrayList<>();
            for (Equation equation : equations) {
                eqs.add(equation.copy());
            }
            return new Equations(new HashMap<>(vars), eqs);
        }

        public void apply(char variable, int value) {

            Deque<Tuple2<Character, Integer>> valsToProcess = new ArrayDeque<>();
            valsToProcess.addLast(Tuple2.tuple(variable, value));

            while (!valsToProcess.isEmpty()) {
                Tuple2<Character, Integer> tuple = valsToProcess.removeFirst();

                vars.put(tuple.getV1(), tuple.getV2());

                for (Equation equation : equations) {
                    equation.apply(tuple.getV1(), tuple.getV2());
                    if (equation.getVars().size() == 1) {
                        Character ch = equation.getVars().stream().findFirst().get();
                        int sum = equation.getSum();
                        valsToProcess.addLast(Tuple2.tuple(ch, sum));
                    }
                }
            }

            this.equations = equations.stream().filter(eq -> !eq.getVars().isEmpty()).toList();

        }
    }

    @Getter
    @AllArgsConstructor
    public static class Equation {
        Set<Character> vars;
        int sum;

        public Equation(int sum, char... vars) {
            this.sum = sum;
            this.vars = new HashSet<>();
            for (char var : vars) {
                this.vars.add(var);
            }
        }

        Equation copy() {
            return new Equation(new HashSet<>(vars), sum);
        }

        public void apply(char variable, int value) {
            if (vars.contains(variable)) {
                vars.remove(variable);
                sum -= value;
            }
        }
    }

    @Getter
    public static class Solution {
        final Map<Character, Integer> vars;

        Solution(Map<Character, Integer> vars) {
            this.vars = Map.copyOf(vars);
        }
    }


}
