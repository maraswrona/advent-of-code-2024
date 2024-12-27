package net.woroniecki.aoc2024;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import groovy.util.logging.Slf4j;
import one.util.streamex.StreamEx;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

@lombok.extern.slf4j.Slf4j
@Slf4j
public class Day23 {

    List<Pair<String, String>> pairs = new ArrayList<>();
    Set<String> computers = new HashSet<>();

    Map<String, Set<String>> connections = new HashMap<>();

    public Day23(String input) {
        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] split = line.trim().split("-");
            String cmp1 = split[0];
            String cmp2 = split[1];
            this.pairs.add(Pair.with(cmp1, cmp2));

            this.computers.add(cmp1);
            this.computers.add(cmp2);

            Set<String> connections1 = this.connections.getOrDefault(cmp1, new HashSet<>());
            connections1.add(cmp2);
            this.connections.put(cmp1, connections1);

            Set<String> connections2 = this.connections.getOrDefault(cmp2, new HashSet<>());
            connections2.add(cmp1);
            this.connections.put(cmp2, connections2);

        }
    }

    public int part1() {
        Set<Set<String>> stack = getInitialGroups();
        Set<Set<String>> allGroups = new HashSet<>();

        while (!stack.isEmpty()) {
            Set<String> group = pop(stack);
            if (group.size() == 3) {
                allGroups.add(group);
                continue;
            }
            stack.addAll(tryToEnlargeGroup(group));
        }

        return (int) StreamEx.of(allGroups).count(group -> group.stream().anyMatch(c -> c.startsWith("t")));
    }


    public String part2() {

        Set<Set<String>> stack = getInitialGroups();
        Set<Set<String>> allGroups = new HashSet<>();

        while (!stack.isEmpty()) {
            Set<String> group = pop(stack);

            Set<Set<String>> newGroups = tryToEnlargeGroup(group);
            if (newGroups.isEmpty()) {
                allGroups.add(group);
            } else {
                stack.addAll(newGroups);
            }
        }

        Set<String> largest = StreamEx.of(allGroups)
                .map(s -> Pair.with(s, s.size()))
                .maxBy(Pair::getValue1)
                .map(Pair::getValue0)
                .orElseThrow();

        return StreamEx.of(largest).sorted().joining(",");
    }

    private Set<Set<String>> getInitialGroups() {
        Set<Set<String>> stack = new HashSet<>();
        for (Pair<String, String> pair : this.pairs) {
            Set<String> group = ImmutableSet.of(pair.getValue0(), pair.getValue1());
            stack.add(group);
        }
        return stack;
    }

    private boolean canBeAdded(String cmp, Set<String> group) {
        return connections.get(cmp).containsAll(group);
    }

    private static Set<String> pop(Set<Set<String>> stack) {
        Iterator<Set<String>> it = stack.iterator();
        Set<String> group = it.next();
        it.remove();
        return group;
    }

    private Set<Set<String>> tryToEnlargeGroup(Set<String> group) {
        Set<Set<String>> result = new HashSet<>();

        Set<String> candidates = group.stream().map(c -> connections.get(c)).flatMap(Collection::stream).collect(Collectors.toSet());
        candidates = Sets.difference(candidates, group);

        for (String cmp : candidates) {
            if (!group.contains(cmp) && canBeAdded(cmp, group)) {
                ImmutableSet.Builder<String> builder = ImmutableSet.builder();
                builder.addAll(group);
                builder.add(cmp);
                ImmutableSet<String> newGroup = builder.build();
                result.add(newGroup);
            }
        }
        return result;
    }

}
