package net.woroniecki.aoc2024;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class Day1 {


    public static void main(String[] args) throws Exception {
        new Day1().part1();
        new Day1().part2();
    }

    private List<Integer> list1;
    private List<Integer> list2;

    private void part1() throws Exception {
        readData();

        log.info("Sorting lists");
        Collections.sort(list1);
        Collections.sort(list2);
        log.info("Sorted");

        int sum = 0;
        for (int i = 0; i < list1.size(); i++) {
            Integer left = list1.get(i);
            Integer right = list2.get(i);
            int dist = Math.abs(left - right);
            sum += dist;
        }
        log.info("Sum: " + sum);
    }

    private Multiset<Integer> counts = HashMultiset.create();

    private void part2() throws Exception {
        readData();
        counts.addAll(list2);

        int sum = 0;
        for (Integer i : list1) {
            int count = counts.count(i);
            int mult = i * count;
            sum += mult;
        }
        log.info("Sum: " + sum);
    }

    private void readData() {
        log.info("Loading data");
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        try {
            File file = new File(this.getClass().getResource("/aoc2024/day1.txt").getFile());
            List<String> lines = FileUtils.readLines(file, "UTF-8");
            for (String line : lines) {
                String[] parts = line.split(" {3}");
                String first = parts[0];
                String second = parts[1];

                Integer int1 = Integer.valueOf(first);
                Integer int2 = Integer.valueOf(second);

                list1.add(int1);
                list2.add(int2);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Data loaded - {} elements", list1.size());
    }


}
