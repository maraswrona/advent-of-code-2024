package net.woroniecki.aoc2025;

import java.util.ArrayList;
import java.util.List;

public class Day01 {

    private final List<String> moves = new ArrayList<>();


    public Day01(String input) {
        for (String line : input.split("\n")) {
            moves.add(line.trim());
        }

    }

    int part1() {

        int pos = 50;
        int zeros = 0;

        for (String move : moves) {
            if (move.startsWith("L")) {
                int n = Integer.parseInt(move.substring(1));

                while (n-- > 0) {
                    if (pos == 0) {
                        pos = 99;
                    } else {
                        pos--;
                    }
                }
            } else if (move.startsWith("R")) {
                int n = Integer.parseInt(move.substring(1));

                while (n-- > 0) {
                    if (pos == 99) {
                        pos = 0;
                    } else {
                        pos++;
                    }
                }
            }

            if(pos == 0) {
                zeros++;
            }
        }

        return zeros;
    }

    int part2() {

        int pos = 50;
        int zeros = 0;

        for (String move : moves) {
            if (move.startsWith("L")) {
                int n = Integer.parseInt(move.substring(1));

                while (n-- > 0) {
                    if (pos == 0) {
                        pos = 99;
                    } else {
                        pos--;
                    }

                    if(pos == 0) {
                        zeros++;
                    }
                }
            } else if (move.startsWith("R")) {
                int n = Integer.parseInt(move.substring(1));

                while (n-- > 0) {
                    if (pos == 99) {
                        pos = 0;
                    } else {
                        pos++;
                    }

                    if(pos == 0) {
                        zeros++;
                    }
                }
            }


        }

        return zeros;
    }

}
