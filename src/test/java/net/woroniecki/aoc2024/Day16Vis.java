package net.woroniecki.aoc2024;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import net.woroniecki.FileUtil;
import java.io.IOException;

public class Day16Vis {

    public static void main(String[] args) throws IOException {
//
//        char [][] lab = new char[20][];
//        StringBuilder sb = new StringBuilder();
//        for (int y = 0; y < 20; y++) {
//            lab[y] = new char[20];
//            for (int x = 0; x < 20; x++) {
//                double rnd = new Random().nextDouble();
//                lab[y][x] = rnd > 0.2 ? '.' : '#';
//            }
//            sb.append(lab[y]).append("\n");
//        }
//        lab[0][0] = 'S';
//        lab[19][19] = 'E';
//
//        sb.replace(0, 1, "S");
//        sb.replace(20*20 - 3, 20*20 - 1, "E");


        Day16 day = new Day16(FileUtil.readFileToString("/aoc2024/day16.txt"));
//        Day16 day = new Day16(sb.toString());
//        Day16 day = new Day16(FileUtil.readFileToString("/aoc2024/day16a.txt"));

//        Day16 day = new Day16("###############\n" +
//                "#.......#....E#\n" +
//                "#.#.###.#.###.#\n" +
//                "#.....#.#...#.#\n" +
//                "#.###.#####.#.#\n" +
//                "#.#.#.......#.#\n" +
//                "#.#.#####.###.#\n" +
//                "#...........#.#\n" +
//                "###.#.#####.#.#\n" +
//                "#...#.....#.#.#\n" +
//                "#.#.#.###.#.#.#\n" +
//                "#.....#...#.#.#\n" +
//                "#.###.#.#.#.#.#\n" +
//                "#S..#.....#...#\n" +
//                "###############");

//        Day16 day = new Day16("#################\n" +
//                "#...#...#...#..E#\n" +
//                "#.#.#.#.#.#.#.#.#\n" +
//                "#.#.#.#...#...#.#\n" +
//                "#.#.#.#.###.#.#.#\n" +
//                "#...#.#.#.....#.#\n" +
//                "#.#.#.#.#.#####.#\n" +
//                "#.#...#.#.#.....#\n" +
//                "#.#.#####.#.###.#\n" +
//                "#.#.#.......#...#\n" +
//                "#.#.###.#####.###\n" +
//                "#.#.#...#.....#.#\n" +
//                "#.#.#.#####.###.#\n" +
//                "#.#.#.........#.#\n" +
//                "#.#.#.#########.#\n" +
//                "#S#.............#\n" +
//                "#################");


        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.getDefaultOfSize(6))
                .setInitialTerminalSize(new TerminalSize(150, 150));
        Screen screen = null;

        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            boolean auto = false;
            boolean running = true;
            int curr = 0;
            while (running) {
                KeyStroke keyStroke = screen.pollInput();
                if (keyStroke != null) {
                    switch (keyStroke.getKeyType()) {
                        case ArrowRight -> day.astar.step();
//                        case ArrowLeft -> day.move(Day15.Move.LEFT);
//                        case ArrowUp -> day.move(Day15.Move.UP);
//                        case ArrowDown -> day.move(Day15.Move.DOWN);
                        case Character -> auto = (keyStroke.getCharacter() == ' ') != auto;
                        case Escape -> running = false;
                        case null, default -> {
                        }
                    }
                }

                if (auto) {
                    for (int i = 0; i < 50; i++) {
                        day.astar.step();
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

    private static void drawGrid(Screen screen, Day16 day) throws IOException {
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
            if (r.ch == '#') {
                textGraphics.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
            } else if (r.ch == '.') {
                textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            }
            textGraphics.putString(r.x, r.y, String.valueOf(r.ch));
        });

        textGraphics.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        day.astar.getClosedList().forEach(n -> {
            textGraphics.putString(n.x, n.y, ",");
        });

        textGraphics.setForegroundColor(TextColor.ANSI.RED);
        day.astar.getOpenList().forEach(n -> {
            textGraphics.putString(n.x, n.y, "*");
        });

        textGraphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        day.astar.reconstructPath(day.astar.getCurrent()).forEach(n -> {
            char ch = switch(n.direction) {
                case DOWN -> 'v';
                case UP -> '^';
                case LEFT -> '<';
                case RIGHT -> '>';
            };
            textGraphics.putString(n.x, n.y, String.valueOf(ch));
        });

        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        Day16.Node curr = day.astar.getCurrent();
        if (curr != null) {
            textGraphics.putString(curr.x, curr.y, "*");
            textGraphics.putString(0, 0, String.format("g: %d h: %d f: %d", curr.g, curr.h, curr.f()));
        }

        screen.refresh();
    }

}


