package net.woroniecki.aoc2024;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class Day21 {

    private PinPad pinpad = new PinPad(
            "789\n" +
                    "456\n" +
                    "123\n" +
                    " 0A"
    );

    private PinPad arrows = new PinPad(
            " ^A\n" +
                    "<v>"
    );

    private final List<String> pins = new ArrayList<>();

    public Day21(String input) {
        for (String line : input.split("\n")) {
            pins.add(line.trim());
        }
    }

    public int part1() {
        return StreamEx.of(pins)
                .mapToInt(pin -> {
                    String moves = pinpad.movesToTypeSequence(pin);
                    for (int i = 0; i < 2; i++) {
                        moves = arrows.movesToTypeSequence(moves);
                    }
                    log.info("{} --> {}", pin, moves);
                    int numeric = Integer.parseInt(pin.substring(0, pin.length() - 1));
                    return moves.length() * numeric;
                })
                .sum();
    }


    public long part2() {
        return StreamEx.of(pins)
                .mapToLong(pin -> {
                    String moves = pinpad.movesToTypeSequence(pin);
                    long cost = cost(moves, 25);
                    log.info("Cost for pin {} is {}", pin, cost);
                    int numeric = Integer.parseInt(pin.substring(0, pin.length() - 1));
                    return cost * numeric;
                })
                .sum();
    }

    private static final Map<String, String> MOVES_LOOKUP = ImmutableMap.<String, String>builder()
            .put("A^", "<A")
            .put("A<", "v<<A")
            .put("Av", "<vA")
            .put("A>", "vA")

            .put("^<", "v<A")
            .put("^>", "v>A")
            .put("^A", ">A")

            .put("<^", ">^A")
            .put("<v", ">A")
            .put("<A", ">>^A")

            .put("v<", "<A")
            .put("v>", ">A")
            .put("vA", "^>A")

            .put(">^", "<^A")
            .put(">v", "<A")
            .put(">A", "^A")
            .build();
    Map<Pair<String, Integer>, Long> cache2 = new HashMap<>();

    public long cost(String sequence, int nprocessors) {
        //log.info("calculating cost of {} at {}", sequence, nprocessors);

        Pair<String, Integer> key = Pair.with(sequence, nprocessors);
        if (cache2.containsKey(key)) {
            return cache2.get(key);
        }

        if (nprocessors == 0) {
            int cost = sequence.length();
            cache2.put(key, (long) cost);
            return cost;
        }

        long cost = 0;
        sequence = "A" + sequence;
        for (int i = 0; i < sequence.length() - 1; i++) {
            String step = sequence.substring(i, i + 2);
            //log.info("Step {}", step);
            final String nextSequence;
            if (step.charAt(0) == step.charAt(1)) {
                nextSequence = "A";
            } else {
                nextSequence = MOVES_LOOKUP.get(step);
            }
            cost += cost(nextSequence, nprocessors - 1);
        }
        cache2.put(key, cost);
        return cost;
    }


    public enum Move {
        UP('^'),
        DOWN('v'),
        LEFT('<'),
        RIGHT('>'),
        A('A');

        private final char ch;

        Move(char ch) {
            this.ch = ch;
        }

        public static List<Move> fromDx(int dx) {
            return repeat(dx < 0 ? LEFT : RIGHT, Math.abs(dx));
        }

        public static List<Move> fromDy(int dy) {
            return repeat(dy < 0 ? UP : DOWN, Math.abs(dy));
        }

        public static List<Move> repeat(Move move, int times) {
            return IntStream.range(0, times)
                    .mapToObj(i -> move)
                    .collect(Collectors.toList());
        }
    }

    public static class PinPad {

        private final Button[][] grid;
        private Button forbidden;
        private Button A;

        PinPad(String layout) {
            String[] lines = layout.split("\n");
            grid = new Button[lines.length][];
            for (int y = 0; y < lines.length; y++) {
                char[] chars = lines[y].toCharArray();
                grid[y] = new Button[chars.length];
                for (int x = 0; x < grid[y].length; x++) {
                    grid[y][x] = new Button(x, y, chars[x]);

                    if (grid[y][x].ch == ' ') {
                        forbidden = grid[y][x];
                    }
                }
            }

        }

        Button find(char ch) {
            return all().filter(b -> b.ch == ch).findFirst().get();
        }

        List<Move> moves(Button from, Button to) {
            int dx = to.x - from.x;
            int dy = to.y - from.y;

            List<Move> xmoves = Move.fromDx(dx);
            List<Move> ymoves = Move.fromDy(dy);


            if (forbidden.y == from.y && forbidden.x == to.x) {
                // we will cross forbidden xmoves, have to go Y first then X
                return StreamEx.of(ymoves).append(xmoves).toList();
            }

            if (forbidden.y == to.y && forbidden.x == from.x) {
                // we will cross forbidden block, have to go X first then Y
                return StreamEx.of(xmoves).append(ymoves).toList();
            }

            if (dx < 0) {
                // there is move left involved, prefer to do it first (X) then up/down (Y)
                // because ending on arrow < is costly to move to A
                return StreamEx.of(xmoves).append(ymoves).toList();
            }

            if (dx > 0 && dy > 0) {
                // this time prefer going down (Y) then right (X)
                return StreamEx.of(ymoves).append(xmoves).toList();
            }

            // all other cases do not matter
            return StreamEx.of(ymoves).append(xmoves).toList();
        }

        public String movesToTypeSequence(String sequence) {
            StringBuilder answer = new StringBuilder();
            char[] charArray2 = sequence.toCharArray();
            Button from = this.find('A');
            for (char ch : charArray2) {
                Button to = this.find(ch);
                List<Move> moves = this.moves(from, to);
                //log.info("Move from {} to {} requires {}", from.ch, to.ch, moves);

                for (Move move : moves) {
                    answer.append(move.ch);
                }
                answer.append('A');

                from = to;
            }
            return answer.toString();
        }

        public Stream<Button> all() {
            return Arrays.stream(grid).flatMap(Arrays::stream);
        }

    }

    @AllArgsConstructor
    public static class Button {
        int x, y;
        char ch;
    }
}



