package net.woroniecki.aoc2025;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Day03 {

    List<List<Integer>> banks;

    public Day03(String input) {
        banks = Arrays.stream(input.split("\n"))
                .map(String::trim)
                .map(s -> {
                    List<Integer> l = new ArrayList<>();
                    for (char c : s.toCharArray()) {
                        l.add(Integer.parseInt("" + c));
                    }
                    return l;
                })
                .toList();
    }

    public long part1() {
        return maxNumber(2);
    }


    public long part2() {
        return maxNumber(12);
    }

    private long maxNumber(int digits) {
        return banks.stream()
                .mapToLong(bank -> {
                    log.info("Checking bank {}", bank);

                    int size = bank.size();
                    long number = 0;

                    int prev = -1;
                    for (int i = digits; i >= 1; i--) {

                        int max = 0;
                        int p2 = prev;
                        for (int j = size - i; j >= p2 + 1; j--) {
                            int test = bank.get(j);
                            if (test >= max) {
                                max = test;
                                prev = j;
                            }
                        }

                        number = number * 10 + max;
                    }
                    log.info("max number {}", number);
                    return number;
                }).sum();
    }


}
