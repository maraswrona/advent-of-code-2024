package net.woroniecki.aoc2025;

import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Day02 {

    List<Pair<Long, Long>> ranges;

    public Day02(String input) {
        String[] ranges = input.split(",");

        this.ranges = Arrays.stream(ranges).map(str -> {
            String[] range = str.split("-");
            Long from = Long.parseLong(range[0]);
            Long till = Long.parseLong(range[1]);
            return new Pair<>(from, till);
        }).toList();
    }

    public long part1() {
        long sum = 0;
        for (Pair<Long, Long> range : ranges) {
            log.info("doing range {} ({} numbers to check)", range, range.getValue1() - range.getValue0());
            Long from = range.getValue0();
            Long till = range.getValue1();

            for (Long i = from; i <= till; i++) {

                String id = Long.toString(i);
                int length = id.length();
                if (length % 2 != 0) {
                    continue;
                }

                String p1 = id.substring(0, length / 2);
                String p2 = id.substring(length / 2, length);
                if (p1.equals(p2)) {
                    sum += i;
                }
            }
        }

        return sum;
    }

    public long part2() {
        long sum = 0;
        for (Pair<Long, Long> range : ranges) {
            log.info("doing range {} ({} numbers to check)", range, range.getValue1() - range.getValue0());
            Long from = range.getValue0();
            Long till = range.getValue1();

            for (Long i = from; i <= till; i++) {

                String id = Long.toString(i);
                int length = id.length();

//                log.info("splitting {}", id);

                boolean idWeird = false;

                for (int n = 2; n <= length && !idWeird; n++) {
                    if (length % n != 0) {
                        continue;
                    }

//                    log.info("dividing by {}", n);

                    int partSize = length / n;

//                    log.info("part size {}", partSize);

                    List<String> parts = new ArrayList<>();
                    for (int j = 0; j <= length - partSize; j += partSize) {
                        parts.add(id.substring(j, j + partSize));
                    }

//                    log.info("parts {}", parts);

                    String part = parts.getFirst();
                    boolean allSame = parts.stream().allMatch(s -> s.equals(part));
                    if (allSame) {
                        log.info("parts all same !!!! {} {} {}", parts, n, id);
                        idWeird = true;
                    }
                }

                if(idWeird) {
                    sum += i;
                }

            }
        }

        return sum;
    }


}
