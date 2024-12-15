package net.woroniecki.aoc2024;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import net.woroniecki.Util;

import java.io.IOException;

public class Day15Vis {

    public static void main(String[] args) throws IOException {

        Day15 day = new Day15(Util.readFileToString("/aoc2024/day15.txt"));


//        Day15 day = new Day15(
//                "########\n" +
//                        "#..O.O.#\n" +
//                        "##@.O..#\n" +
//                        "#...O..#\n" +
//                        "#.#.O..#\n" +
//                        "#...O..#\n" +
//                        "#......#\n" +
//                        "########\n" +
//                        "\n" +
//                        "<^^>>>vv<v>>v<<");


        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.getDefaultOfSize(15))
                .setInitialTerminalSize(new TerminalSize(50, 50));
        Screen screen = null;


        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            boolean running = true;
            while (running) {
                KeyStroke keyStroke = screen.pollInput();
                if (keyStroke != null) {
                    switch (keyStroke.getKeyType()) {
                        case ArrowRight -> day.move(Day15.Move.RIGHT);
                        case ArrowLeft -> day.move(Day15.Move.LEFT);
                        case ArrowUp -> day.move(Day15.Move.UP);
                        case ArrowDown -> day.move(Day15.Move.DOWN);
                        case Escape -> running = false;
                        case null, default -> {
                        }
                    }
                }

                drawGrid(screen, day);
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

    private static void drawGrid(Screen screen, Day15 day) throws IOException {
        TextGraphics textGraphics = screen.newTextGraphics();
        screen.clear();

//        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
//        for (int y = 0; y < day.grid.length; y++) {
//            for (int x = 0; x < day.grid[y].length; x++) {
//                textGraphics.putString(x, y, ".");
//            }
//        }

        //textGraphics.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);

//        for (int y = 0; y < day.h; y++) {
//            textGraphics.putString(0, y, String.valueOf(y));
//        }
//
//        for (int x = 0; x < day.w; x++) {
//            textGraphics.putString(x, 0, String.valueOf(x));
//        }

//        textGraphics.putString(0, 0, String.valueOf(time));

        day.allBlocks().forEach(r -> {
            if (r.hasBox()) {
                textGraphics.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
            } else if (r.hasWall()) {
                textGraphics.setForegroundColor(TextColor.ANSI.MAGENTA);
            } else if (r.hasRobot()) {
                textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
            } else {
                textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            }
            textGraphics.putString(r.x, r.y, String.valueOf(r.ch));
        });

        screen.refresh();
    }

}


