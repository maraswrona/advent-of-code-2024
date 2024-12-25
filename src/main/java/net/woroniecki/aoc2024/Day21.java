package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class Day21 {

    // programmer --> robot --> robot --> robot --> pinpad

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
                    String moves = movesToTypeSequence(pin, pinpad);
                    for (int i = 0; i < 2; i++) {
                        moves = movesToTypeSequence(moves, arrows);
                    }
                    log.info("{} --> {}", pin, moves);
                    int numeric = Integer.parseInt(pin.substring(0, pin.length() - 1));
                    return moves.length() * numeric;
                })
                .sum();
    }

    public int part2() {
        return StreamEx.of(pins)
                .mapToInt(pin -> {
                    String moves = movesToTypeSequence(pin, pinpad);
                    for (int i = 0; i < 10; i++) {
                        log.info("doing pin {} iteration {}", pin, i);
                        moves = movesToTypeSequence(moves, arrows);
                        log.info("got {} moves: {}", moves.length(), moves);
                    }
                    log.info("{} --> {}", pin, moves);
                    int numeric = Integer.parseInt(pin.substring(0, pin.length() - 1));
                    return moves.length() * numeric;
                })
                .sum();
    }

    public String movesToTypeSequence(String sequence, PinPad buttons) {
        StringBuilder answer = new StringBuilder();
        char[] charArray2 = sequence.toCharArray();
        Button from = buttons.find('A');
        for (char ch : charArray2) {
            Button to = buttons.find(ch);
            List<Move> moves = buttons.moves(from, to);
            //log.info("Move from {} to {} requires {}", from.ch, to.ch, moves);

            for (Move move : moves) {
                answer.append(move.ch);
            }
            answer.append('A');

            from = to;
        }
        return answer.toString();
    }


    public enum Move {
        UP('^', 0, -1),
        DOWN('v', 0, 1),
        LEFT('<', -1, 0),
        RIGHT('>', 1, 0);

        private final char ch;
        private final int dx;
        private final int dy;

        Move(char ch, int dx, int dy) {
            this.ch = ch;
            this.dx = dx;
            this.dy = dy;
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

        Button selected;
        boolean pressed;

        PinPad nextPinPad;

        List<Character> output = new ArrayList<>();


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

            this.selected = find('A');
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
            return StreamEx.of(xmoves).append(ymoves).toList();
        }


        public Stream<Button> all() {
            return Arrays.stream(grid).flatMap(Arrays::stream);
        }

        public void moveSelected(Move move) {
            int nx = selected.x + move.dx;
            int ny = selected.y + move.dy;
            if (checkCoords(nx, ny)) {
                selected = grid[ny][nx];
            }
        }

        private boolean checkCoords(int x, int y) {
            return x >= 0 && x < grid[0].length &&
                    y >= 0 && y < grid.length;
        }

        public void press() {
            this.pressed = true;

            if (nextPinPad != null) {
                switch (selected.ch) {
                    case 'A' -> nextPinPad.press();
                    case '>' -> nextPinPad.moveSelected(Move.RIGHT);
                    case '<' -> nextPinPad.moveSelected(Move.LEFT);
                    case '^' -> nextPinPad.moveSelected(Move.UP);
                    case 'v' -> nextPinPad.moveSelected(Move.DOWN);
                }
            } else {
                output.add(selected.ch);
            }

        }

        public void depress() {
            this.pressed = false;
        }

        public void toggle() {
            this.pressed = !pressed;
        }


        public void reset() {
            selected = find('A');
            pressed = false;
            output.clear();
        }
    }

    @AllArgsConstructor
    public static class Button {
        int x, y;
        char ch;
    }


}
