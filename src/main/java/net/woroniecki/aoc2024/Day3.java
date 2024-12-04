package net.woroniecki.aoc2024;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

    private final String input;

    public Day3(String input) {
        this.input = input;
    }

    public int part1() {
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher matcher = pattern.matcher(input);

        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }

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

        return sum;

    }

    public int part2() {

        Pattern pattern = Pattern.compile("(?<mul>mul\\((\\d{1,3}),(\\d{1,3})\\))|(?<do>do\\(\\))|(?<dont>don't\\(\\))");
        Matcher matcher = pattern.matcher(input);

        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }

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

      return sum;

    }

}
