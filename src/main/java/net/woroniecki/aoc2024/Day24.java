package net.woroniecki.aoc2024;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class Day24 {

    Circuit circuit;

    static class Circuit {
        final Map<String, Boolean> variables;
        final ImmutableMap<String, Boolean> originalVars;
        final Map<String, String> renames = new HashMap<>();
        final List<Operation> operations;
        final int bits;

        public Circuit(Map<String, Boolean> variables, List<Operation> operations) {
            this.variables = variables;
            this.originalVars = ImmutableMap.copyOf(variables);
            this.operations = operations;
            long bits = operations.stream()
                    .flatMap(op -> Stream.of(op.var1, op.var2, op.outVar))
                    .distinct()
                    .filter(v -> v.matches("z\\d\\d"))
                    .count();
            this.bits = (int) bits;

            Collections.sort(operations, Ordering.natural().<Operation>onResultOf(op -> {
                return Stream.of(op.var1, op.var2, op.outVar)
                        .filter(v -> v.matches("([xyz])\\d\\d"))
                        .map(v -> v.substring(1))
                        .findFirst().orElse(op.var1);
            }));
        }

        public void swap(String out1, String out2) {
            Operation op1 = find(out1);
            Operation op2 = find(out2);

            String tmp = op1.outVar;
            op1.outVar = op2.outVar;
            op2.outVar = tmp;
        }

        public String rn(String var) {
            return renames.getOrDefault(var, var);
        }

        public List<Operation> sorted() {
            List<Operation> results = new ArrayList<>();
            Set<String> usedOutputs = new HashSet<>();

            for (int i = 0; i < bits; i++) {
                // find z
                String zout = String.format("z%02d", i);
                Operation zop = find(zout);

                Deque<String> opsToFind = new ArrayDeque<>();
                opsToFind.add(zop.var1);
                opsToFind.add(zop.var2);

                List<Operation> localOps = new ArrayList<>();
                localOps.add(zop);

                while (!opsToFind.isEmpty()) {
                    String opout = opsToFind.pop();
                    if (opout.matches("[xy]\\d{2}")) {
                        continue;
                    }
                    if (usedOutputs.contains(opout)) {
                        continue;
                    }

                    Operation op = find(opout);
                    opsToFind.add(op.var1);
                    opsToFind.add(op.var2);
                    localOps.add(op);
                    usedOutputs.add(opout);
                }

                for (Operation op : localOps) {
                    if (op.outVar.equals(zout)) continue;

                    switch (op.op) {
                        case "XOR" -> renames.put(op.outVar, zout + "XOR");
                        case "OR" -> renames.put(op.outVar, zout + "OR");
                        case "AND" -> {
                            if (op.var1.matches("[xy]\\d{2}")) {
                                renames.put(op.outVar, zout + "AND1");
                            } else {
                                renames.put(op.outVar, zout + "AND2");
                            }
                        }
                    }
                }

                Ordering<Operation> order = Ordering.explicit(zout, zout + "OR", zout + "AND2", zout + "XOR", zout + "AND1").onResultOf(op -> renames.getOrDefault(op.outVar, op.outVar));
                localOps.sort(order);

                results.addAll(localOps);
            }

            return results;
        }

        private Operation find(String out) {
            return StreamEx.of(operations)
                    .findAny($ -> $.outVar.equals(out))
                    .orElseThrow(() -> new IllegalStateException("Operation not found for result " + out));
        }

        public List<Operation> dependencies(String output) {

            List<Operation> result = new ArrayList<>();
            Operation op = find(output);
            result.add(op);
            if (!op.var1.matches("([xy])\\d\\d")) {
                result.addAll(dependencies(op.var1));
            }

            if (!op.var2.matches("([xy])\\d\\d")) {
                result.addAll(dependencies(op.var2));
            }

            return result;
        }

        public String equation(String output) {

            Operation op = find(output);

            String var1 = op.var1;
            if (!var1.matches("([xy])\\d\\d")) {
                var1 = String.format("(%s)", equation(var1));
            }
            String var2 = op.var2;
            if (!var2.matches("([xy])\\d\\d")) {
                var2 = String.format("(%s)", equation(var2));
            }
            return String.format("%s %s %s", var1, op.op, var2);
        }

        public long calculate() {
            Deque<Operation> stack = new LinkedList<>();
            stack.addAll(operations);

            int order = 0;
            while (!stack.isEmpty()) {
                Operation op = stack.pop();
                //log.info("Doing: {} - still {} to go", op, stack.size());

                if (variables.containsKey(op.var1) && variables.containsKey(op.var2)) {
                    boolean var1 = variables.get(op.var1);
                    boolean var2 = variables.get(op.var2);

                    boolean out = switch (op.op) {
                        case "AND" -> var1 && var2;
                        case "OR" -> var1 || var2;
                        case "XOR" -> var1 ^ var2;
                        default -> throw new IllegalStateException("Unexpected value: " + op.op);
                    };

                    variables.put(op.outVar, out);
                    op.order = order++;
                } else {
                    stack.add(op);
                }
            }

            Collections.sort(operations, Ordering.natural().onResultOf(o -> o.order));

            String binary = EntryStream.of(variables)
                    .filterKeys(k -> k.startsWith("z"))
                    .reverseSorted(Ordering.natural().onResultOf(Map.Entry::getKey))
                    .values()
                    .map(b -> b ? "1" : "0")
                    .joining();
            return Long.parseLong(binary, 2);
        }

        public void reset() {
            this.variables.clear();
            this.variables.putAll(this.originalVars);
        }

        public void clear() {
            this.variables.clear();
        }

        public void setX(long x) {
            setVar(x, "x");
        }

        public void setY(long y) {
            setVar(y, "y");
        }

        public void setVar(long val, String var) {
            for (int i = 0; i < bits; i++) {
                String varName = String.format("%s%02d", var, i);

                this.variables.put(varName, (val >> i) % 2 == 1);
            }
        }
    }

    @AllArgsConstructor
    @ToString
    public static class Operation {
        final String var1;
        final String var2;
        final String op;
        String outVar;
        int order;
    }

    public Day24(String input) {
        Map<String, Boolean> variables = new HashMap<>();
        List<Operation> operations = new ArrayList<>();

        String[] lines = input.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.contains(":")) {
                String[] parts = line.split(": ");
                String var1 = parts[0].trim();
                boolean value = Objects.equals(parts[1].trim(), "1");
                variables.put(var1, value);
            }
            if (line.contains("->")) {
                Pattern pattern = Pattern.compile("(?<var1>[a-z0-9]{3}) (?<op>[A-Z]{2,3}) (?<var2>[a-z0-9]{3}) -> (?<out>[a-z0-9]{3})");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String var1 = matcher.group("var1").trim();
                    String op = matcher.group("op").trim();
                    String var2 = matcher.group("var2").trim();
                    String outVar = matcher.group("out").trim();
                    operations.add(new Operation(var1, var2, op, outVar, 0));
                }
            }
        }

        this.circuit = new Circuit(variables, operations);
    }

    public long part1() {
        return circuit.calculate();
    }

    public String part2() {
        return StreamEx.of("gws", "nnt", "z13", "npf", "cph", "z19", "hgj", "z33").sorted().joining(",");
    }


}
