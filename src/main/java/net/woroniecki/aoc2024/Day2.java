package net.woroniecki.aoc2024;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Day2 {


    public static void main(String[] args) throws Exception {
        new Day2().part1();
        new Day2().part2();
    }

    private List<List<Integer>> reports;

    private void part1() throws Exception {
        readData();
        int sum = reports.stream()
                .mapToInt(r -> isSafe(r) ? 1 : 0)
                .sum();
        log.info("Safe reports: {}", sum);
    }

    private void part2() throws Exception {
        readData();
        int sum = reports.stream()
                .mapToInt(r -> isSafe2(r) ? 1 : 0)
                .sum();
        log.info("Safe reports: {}", sum);
    }

    private boolean isSafe2(List<Integer> report) {
        if (isSafe(report)) {
            return true;
        }
        for (int i = 0; i < report.size(); i++) {
            List<Integer> copy = new ArrayList<>(report);
            copy.remove(i);
            if(isSafe(copy)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSafe(List<Integer> report) {
        boolean increasing = report.get(0) < report.get(1);
        for (int i = 0; i < report.size() - 1; i++) {
            int a = report.get(i);
            int b = report.get(i + 1);
            int diff = (a - b);
            int diff2 = Math.abs(diff);
            if (diff2 == 0 || diff2 > 3) {
                return false;
            }

            if (increasing != diff < 0) {
                return false;
            }
        }
        return true;
    }

    private void readData() {
        log.info("Loading data");
        reports = new ArrayList<>();
        try {
            File file = new File(this.getClass().getResource("/aoc2024/day2.txt").getFile());
            List<String> lines = FileUtils.readLines(file, "UTF-8");
            for (String line : lines) {
                String[] numbers = line.split(" ");
                List<Integer> report = new ArrayList<>();
                for (String number : numbers) {
                    Integer i = Integer.parseInt(number);
                    report.add(i);
                }
                reports.add(report);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Data loaded - {} reports", reports.size());
    }


}
