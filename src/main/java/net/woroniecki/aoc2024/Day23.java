package net.woroniecki.aoc2024;

import com.google.common.collect.Sets;
import groovy.util.logging.Slf4j;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@lombok.extern.slf4j.Slf4j
@Slf4j
public class Day23 {

    //BiMap<String, String> input = HashBiMap.create();
    Set<String> computers = new HashSet<>();

    Map<String, Set<String>> connections = new HashMap<>();

    public Day23(String input) {
        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] split = line.trim().split("-");
            String cmp1 = split[0];
            String cmp2 = split[1];
            //this.input.put(cmp1, cmp2);

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

        int found = 0;

        Set<Set<String>> groups = new HashSet<>();
        for (String cmp : this.computers) {
            log.info("checking cmp {} has {}", cmp, this.connections.get(cmp));
            Set<String> group = new HashSet<>();
            group.add(cmp);
            for (String cmp2 : this.connections.get(cmp)) {
                if (canBeAdded(cmp2, group)) {
                    group.add(cmp2);
                }
            }
            System.out.println(group);

            if (group.size() > 3) {
                Set<Set<String>> split = Sets.combinations(group, 3);
                groups.addAll(split);
            } else if (group.size() == 3) {
                groups.add(group);

            }

        }

        for (Set<String> group : groups) {
            if (group.stream().anyMatch(c -> c.contains("t"))) {
                found++;
                System.out.println("found: " + group.stream().sorted().toList());
            }
        }

        return found;
    }

    private boolean canBeAdded(String cmp, Set<String> group) {
        return connections.get(cmp).containsAll(group);
    }

    public int part2() {
        return 0;
    }

}
