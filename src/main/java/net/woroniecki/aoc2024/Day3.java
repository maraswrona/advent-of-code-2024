package net.woroniecki.aoc2024;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Day3 {

    public static void main(String[] args) {
        new Day3().part2();
    }

    private void part1() {
        String txt = loadData();

        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher matcher = pattern.matcher(txt);

        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        log.info("{}", matches);

        int sum = matches.stream()
                .mapToInt(mul -> {
                    Matcher m2 = pattern.matcher(mul);
                    if (m2.matches()) {
                        String a = m2.group(1);
                        String b = m2.group(2);

                        Integer i = Integer.valueOf(a);
                        Integer j = Integer.valueOf(b);

                        return i * j;
                    }
                    return 0;
                }).sum();

        log.info("{}", sum);

    }

    private void part2() {
        String txt = loadData();

        Pattern pattern = Pattern.compile("(?<mul>mul\\((\\d{1,3}),(\\d{1,3})\\))|(?<do>do\\(\\))|(?<dont>don't\\(\\))");
        Matcher matcher = pattern.matcher(txt);

        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        log.info("{}", matches);

        AtomicBoolean enabled = new AtomicBoolean(true);
        int sum = matches.stream()
                .mapToInt(mul -> {
                    if(mul.startsWith("mul") && enabled.get()) {
                        Matcher m2 = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)").matcher(mul);
                        if (m2.matches()) {
                            String a = m2.group(1);
                            String b = m2.group(2);

                            Integer i = Integer.valueOf(a);
                            Integer j = Integer.valueOf(b);

                            return i * j;
                        }
                        return 0;
                    }
                    if(mul.equals("do()")) {
                        enabled.set(true);
                    }
                    if(mul.equals("don't()")) {
                        enabled.set(false);
                    }
                    return 0;
                }).sum();

        log.info("{}", sum);

    }

    private String loadData() {
        try {
            File file = new File(this.getClass().getResource("/aoc2024/day3.txt").getFile());
            String txt = FileUtils.readFileToString(file, "UTF-8");
            return txt;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
