package net.woroniecki.aoc2024;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import one.util.streamex.StreamEx;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Day21Vis {

    public static void main(String[] args) throws IOException {

        Day21.PinPad pinpad = new Day21.PinPad(
                "789\n" +
                        "456\n" +
                        "123\n" +
                        " 0A"
        );

        Day21.PinPad arrows1 = new Day21.PinPad(
                " ^A\n" +
                        "<v>"

        );


        Day21.PinPad arrows2 = new Day21.PinPad(
                " ^A\n" +
                        "<v>"
        );


        Day21.PinPad arrows3 = new Day21.PinPad(
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
                                arrows3.moveSelected(Day21.Move.RIGHT);
                            } else {
                                arrows3.selected = arrows3.find('>');
                                arrows3.press();
                                lastPressed = Instant.now();
                            }
                        }
                        case ArrowLeft -> {
                            if(keyStroke.isCtrlDown()) {
                                arrows3.moveSelected(Day21.Move.LEFT);
                            } else {
                                arrows3.selected = arrows3.find('<');
                                arrows3.press();
                                lastPressed = Instant.now();
                            }
                        }
                        case ArrowUp -> {
                            if(keyStroke.isCtrlDown()) {
                                arrows3.moveSelected(Day21.Move.UP);
                            } else {
                                arrows3.selected = arrows3.find('^');
                                arrows3.press();
                                lastPressed = Instant.now();
                            }
                        }
                        case ArrowDown -> {
                            if(keyStroke.isCtrlDown()) {
                                arrows3.moveSelected(Day21.Move.DOWN);
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

    private static void drawGrid(Screen screen, Day21.PinPad grid, Day21.PinPad arrows1, Day21.PinPad arrows2, Day21.PinPad arrows3) throws IOException {
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

    private static void drawGrid(Day21.PinPad grid, TextGraphics textGraphics, int x, int y) {
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

}


