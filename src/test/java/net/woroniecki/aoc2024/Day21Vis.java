package net.woroniecki.aoc2024;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import lombok.AllArgsConstructor;
import one.util.streamex.StreamEx;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day21Vis {

    public static void main(String[] args) throws IOException {

        PinPad pinpad = new PinPad(
                "789\n" +
                        "456\n" +
                        "123\n" +
                        " 0A"
        );

        PinPad arrows1 = new PinPad(
                " ^A\n" +
                        "<v>"

        );


        PinPad arrows2 = new PinPad(
                " ^A\n" +
                        "<v>"
        );


        PinPad arrows3 = new PinPad(
                " ^A\n" +
                        "<v>"
        );

        arrows1.nextPinPad = pinpad;
        arrows2.nextPinPad = arrows1;
        arrows3.nextPinPad = arrows2;

        String pin = "<vAA>^AvAA<^A>AvvA>>^AvA^A<vA>^AvvA>^A>AAvA^AvvA>A>^AAAvA<^A>A";
        int curr = 0;

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.getDefaultOfSize(18))
                .setInitialTerminalSize(new TerminalSize(25, 35));
        Screen screen = null;

        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            boolean running = true;

            Instant lastPressed = Instant.now();
            while (running) {
                KeyStroke keyStroke = screen.pollInput();
                if (keyStroke != null) {
                    switch (keyStroke.getKeyType()) {
                        case ArrowRight ->{
                            if(keyStroke.isCtrlDown()) {
                                arrows3.moveSelected(Move.RIGHT);
                            } else {
                                arrows3.selected = arrows3.find('>');
                                arrows3.press();
                                lastPressed = Instant.now();
                            }
                        }
                        case ArrowLeft -> {
                            if(keyStroke.isCtrlDown()) {
                                arrows3.moveSelected(Move.LEFT);
                            } else {
                                arrows3.selected = arrows3.find('<');
                                arrows3.press();
                                lastPressed = Instant.now();
                            }
                        }
                        case ArrowUp -> {
                            if(keyStroke.isCtrlDown()) {
                                arrows3.moveSelected(Move.UP);
                            } else {
                                arrows3.selected = arrows3.find('^');
                                arrows3.press();
                                lastPressed = Instant.now();
                            }
                        }
                        case ArrowDown -> {
                            if(keyStroke.isCtrlDown()) {
                                arrows3.moveSelected(Move.DOWN);
                            } else {
                                arrows3.selected = arrows3.find('v');
                                arrows3.press();
                                lastPressed = Instant.now();
                            }
                        }

                        case Character -> {
                            if (keyStroke.getCharacter() == 'a') {
                                arrows3.selected = arrows3.find('A');
                                arrows3.press();
                                lastPressed = Instant.now();
                            }
                            if (keyStroke.getCharacter() == ' ') {
                                arrows3.press();
                                lastPressed = Instant.now();
                            }
                        }
                        case Enter -> {
                            char ch = pin.charAt(curr);
                            arrows3.selected = arrows3.find(ch);
                            arrows3.press();
                            lastPressed = Instant.now();
                            if (curr < pin.length()-1) {
                                curr++;
                            }
                        }
                        case Backspace -> {
                            curr = 0;
                            arrows3.reset();
                            arrows2.reset();
                            arrows1.reset();
                            pinpad.reset();

                        }
                        case Escape -> running = false;
                        case null, default -> {
                        }
                    }
                }


                if (Duration.between(lastPressed, Instant.now()).toMillis() > 750) {
                    arrows1.depress();
                    arrows2.depress();
                    arrows3.depress();
                    pinpad.depress();
                }

                drawGrid(screen, pinpad, arrows1, arrows2, arrows3);
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void drawGrid(Screen screen, PinPad grid, PinPad arrows1, PinPad arrows2, PinPad arrows3) throws IOException {
        TextGraphics textGraphics = screen.newTextGraphics();
        screen.clear();

        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        String pin = StreamEx.of(grid.output).joining();
        textGraphics.putString(1, 1, pin);

        drawGrid(grid, textGraphics, 3, 3);
        drawGrid(arrows1, textGraphics, 3, 13);
        drawGrid(arrows2, textGraphics, 3, 19);
        drawGrid(arrows3, textGraphics, 3, 25);

        screen.refresh();
    }

    private static void drawGrid(PinPad grid, TextGraphics textGraphics, int x, int y) {
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        grid.all().forEach(b -> {
            textGraphics.putString(x + 4 * b.x, y + 2 * b.y, "+---+");
            textGraphics.putString(x + 4 * b.x, y + 2 * b.y + 1, "| " + b.ch + " |");
            textGraphics.putString(x + 4 * b.x, y + 2 * b.y + 2, "+---+");
        });

        if (grid.pressed) {
            textGraphics.setBackgroundColor(TextColor.ANSI.GREEN);
        }

        textGraphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        textGraphics.putString(x + 4 * grid.selected.x, y + 2 * grid.selected.y, "+---+");
        textGraphics.putString(x + 4 * grid.selected.x, y + 2 * grid.selected.y + 1, "| " + grid.selected.ch + " |");
        textGraphics.putString(x + 4 * grid.selected.x, y + 2 * grid.selected.y + 2, "+---+");
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

    public enum Move {
        UP('^', 0, -1),
        DOWN('v', 0, 1),
        LEFT('<', -1, 0),
        RIGHT('>', 1, 0),
        A('A', 0, 0);

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

}


