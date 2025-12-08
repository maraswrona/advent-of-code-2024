package net.woroniecki.aoc2025;

import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Day05 {

    List<Range<Long>> ranges = new ArrayList<>();
    List<Long> ingredients = new ArrayList<>();

    public Day05(String input) {
        String[] lines = input.split("\n");
        boolean r = true;
        for (String l : lines) {

            if (l.trim().isEmpty()) {
                r = false;
                continue;
            }

            if (r) {
                String[] nums = l.trim().split("-");
                long from = Long.parseLong(nums[0]);
                long till = Long.parseLong(nums[1]);
                ranges.add(Range.closed(from, till));
            } else {
                ingredients.add(Long.parseLong(l.trim()));
            }
        }

    }

    public int part1() {

        int cnt = 0;
        for (long ing : ingredients) {
            for (Range<Long> rng : ranges) {
                if (rng.contains(ing)) {
                    cnt++;
                    break;
                }
            }
        }
        return cnt;
    }

    public long part2() {

        LinkedList<Range<Long>> toProcess = new LinkedList<>(ranges);
        List<Range<Long>> merged = new ArrayList<>();

        while (!toProcess.isEmpty()) {
            Range<Long> r = toProcess.removeFirst();

            List<Range<Long>> connected = new ArrayList<>();
            List<Range<Long>> notConnected = new ArrayList<>();

            for (Range<Long> candidate : merged) {
                if (candidate.isConnected(r)) {
                    connected.add(candidate);
                } else {
                    notConnected.add(candidate);
                }
            }

            for (Range<Long> candidate : connected) {
                r = r.span(candidate);
            }

            merged.clear();
            merged.addAll(notConnected);
            merged.add(r);
        }

        long sum = 0;
        for (Range<Long> r : merged) {
            long size = r.upperEndpoint() - r.lowerEndpoint() + 1;
            sum += size;
        }

        return sum;
    }


}
