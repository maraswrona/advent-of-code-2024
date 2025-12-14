package net.woroniecki.aoc2025;

import groovy.lang.Tuple2;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class Day10 {

    @AllArgsConstructor
    private class Case {
        String lights;
        List<List<Byte>> buttons;
        List<Short> joltage;
    }

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

        return EntryStream.of(cases)
                .peek(e -> log.info("processing case {}", e.getKey()))
                .map(Map.Entry::getValue)
                .mapToInt(c -> {
                    Map<Character, List<Byte>> buttons = EntryStream.of(c.buttons)
                            .mapKeys(k -> (char) ('A' + k))
                            .toMap();

                    Set<Equation> equations = EntryStream.of(c.joltage)
                            .map(e -> {
                                Set<Character> variables = EntryStream.of(buttons)
                                        .filterValues(v -> v.contains(e.getKey().byteValue()))
                                        .keys()
                                        .toSet();

                                return new Equation(variables, e.getValue());
                            })
                            .toSet();

                    return new Solver().solve(new Equations(equations));
                })
                .sum();
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


    public static class Solver {

        public int solve(Equations equations) {

            // this is kind of "generic" solver for sets of equations of form A + B + C = val
            // we know that the puzzle limitation tells us the val is always non-negative number
            // also all the coefficients for all equations are always equal to 1
            // here is what we do to solve it:

            // 1. try to "expand" a query as much as possible
            // (if one equation contains another, then we can create a difference and keep as new equation, could help to narrow down the solution)
            // e.g. if A+B+C+D=10 and A+B=5 therefore C+D=5 (we simply subtract second from first)

            // 2. if there are any reduced equation in form of A = val then immediately assign this val to all other equations and simplify them

            // 3. if we cannot simplify or substitute anymore, take smallest equation (least number of variables + lowest sum)
            // and try to "guess" one of its variables by simply iterating from 0 till the sum
            // each time create new copy of equations object, assign the val, reduce as much as possible and continue with 1, 2 and 3 until
            // either the equations are solved and all the variables found (correct solution)
            // or equations starts to contradict itself - in such case stop processing this particular version and move to next one

            // at every step of this procedure after any change or assignment all equations must be valid
            // e.g. no 2 contradicting equations (same vars, different sum)
            // no equation goes < 0
            // already solved variable with value x cannot have different solution
            // if any of rules is broken - entire equation set is immediately invalid and we move on to another version
            // (meaning - we go back to iteration from point 3 and continue with different number)

            // this method produces ALL valid solutions for this particular equations set
            // it records solutions sum of variables and returns a min of it

            Deque<Equations> stack = new ArrayDeque<>();
            stack.push(equations);

            log.info("solving equation: \n {}", equations);

            IntSummaryStatistics solutions = new IntSummaryStatistics();

            while (!stack.isEmpty()) {
                Equations eqs = stack.pop();
                eqs.expand();

                if (!eqs.validate()) {
                    continue;
                }

                if (eqs.isSolved()) {
                    int sum = EntryStream.of(eqs.getVars()).mapToInt(Map.Entry::getValue).sum();
                    if (equations.verify(eqs.getVars())) {
                        solutions.accept(sum);
                    }
                } else {
                    Equation eq = eqs.findSmallest();
                    char variable = eq.getVars().stream().findFirst().get();
                    int sum = eq.getSum();

                    if (eq.getVars().size() == 1) {
                        eqs.apply(variable, sum);
                        stack.push(eqs);
                    } else {
                        for (int value = 0; value <= sum; value++) {
                            Equations copy = eqs.copy();
                            if (copy.apply(variable, value)) {
                                stack.push(copy);
                            }
                        }
                    }
                }
            }
            log.info("solutions {} best {}", solutions.getCount(), solutions.getMin());
            return solutions.getMin();
        }

    }

    @Getter
    public static class Equations {
        Map<Character, Integer> vars = new HashMap<>();
        Set<Equation> equations;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            String vars = EntryStream.of(this.vars)
                    .sortedBy(Map.Entry::getKey)
                    .map(e -> e.getKey() + " = " + e.getValue())
                    .joining("\n");

            String equations = StreamEx.of(this.equations).joining("\n");

            sb.append("Variables:\n");
            sb.append(vars);
            sb.append("Equations:\n");
            sb.append(equations);

            return sb.toString();
        }

        public Equations(Set<Equation> equations) {
            this.equations = equations;
        }

        public Equations(Map<Character, Integer> vars, Set<Equation> equations) {
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

        public boolean verify(Map<Character, Integer> vars) {
            return StreamEx.of(equations).allMatch(e -> e.verify(vars));
        }

        public Equations copy() {
            Set<Equation> eqs = new HashSet<>();
            for (Equation equation : equations) {
                eqs.add(equation.copy());
            }
            return new Equations(new HashMap<>(vars), eqs);
        }

        private boolean expand() {
            boolean changed = true;
            while (changed) {
                Set<Equation> newEquations = new HashSet<>();
                List<Equation> eqs = new ArrayList<>(this.equations);
                for (int i = 0; i < eqs.size(); i++) {
                    Equation eq1 = eqs.get(i);
                    for (int j = i + 1; j < eqs.size(); j++) {
                        Equation eq2 = eqs.get(j);

                        if (eq1.contains(eq2)) {
                            Equation copy = eq1.copy();
                            copy.subtract(eq2);
                            newEquations.add(copy);
                        } else if (eq2.contains(eq1)) {
                            Equation copy = eq2.copy();
                            copy.subtract(eq1);
                            newEquations.add(copy);
                        }
                    }
                }
                int oldSize = this.equations.size();
                this.equations.addAll(newEquations);
                changed = oldSize != this.equations.size();

                if (!validate()) {
                    return false;
                }
            }
            return validate();
        }

        public boolean validate() {
            // validate individual equations
            for (Equation equation : equations) {
                if (!equation.validate()) {
                    return false;
                }
            }

            // check for contradicting equations
            Map<Set<Character>, Integer> tmp = new HashMap<>();
            for (Equation eq : equations) {
                if (!tmp.containsKey(eq.getVars())) {
                    tmp.put(eq.getVars(), eq.getSum());
                } else {
                    if (tmp.get(eq.getVars()) != eq.getSum()) {
                        return false;
                    }
                }
            }

            return true;
        }


        public boolean apply(char variable, int value) {

            Deque<Tuple2<Character, Integer>> queue = new ArrayDeque<>();
            queue.addLast(new Tuple2<>(variable, value));

            while (!queue.isEmpty()) {
                expand();

                Tuple2<Character, Integer> tuple = queue.removeFirst();

                // check for contradiction
                if (vars.containsKey(tuple.getV1()) && !Objects.equals(vars.get(tuple.getV1()), tuple.getV2())) {
                    return false;
                }
                vars.put(tuple.getV1(), tuple.getV2());

                for (Equation equation : equations) {
                    equation.apply(tuple.getV1(), tuple.getV2());

                    if (equation.getVars().size() == 1) {
                        queue.addLast(new Tuple2<>(equation.getVars().iterator().next(), equation.getSum()));
                    }
                }

                Predicate<Equation> empty = equation -> equation.getVars().isEmpty() && equation.getSum() == 0;
                this.equations = StreamEx.of(this.equations)
                        .filter(Predicate.not(empty))
                        .collect(Collectors.toSet());


                if (!validate()) {
                    return false;
                }
            }
            return true;
        }
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Equation {
        Set<Character> vars;
        int sum;

        @Override
        public String toString() {
            return "[ " + StreamEx.of(vars).sorted().joining("+") + " = " + sum + " ]";
        }

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

        public boolean contains(Equation other) {
            return this.vars.size() > other.vars.size() &&
                    this.vars.containsAll(other.vars);
        }

        public void subtract(Equation other) {
            vars.removeAll(other.getVars());
            sum -= other.sum;
        }

        public boolean verify(Map<Character, Integer> vars) {
            return StreamEx.of(this.vars).mapToInt(vars::get).sum() == this.sum;
        }

        public boolean validate() {
            // equation cannot be negative, all values are positive
            if (sum < 0) {
                return false;
            }

            // after all substitutions equation must go to zero
            if (vars.isEmpty() && sum > 0) {
                return false;
            }

            return true;
        }
    }

}
