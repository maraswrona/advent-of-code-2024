package net.woroniecki.aoc2024;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import net.woroniecki.FileUtil;

import java.io.IOException;

public class Day14Vis {

    public static void main(String[] args) throws IOException {

        int w = 101;
        int h = 103;
        Day14 day = new Day14(FileUtil.readFileToString("/aoc2024/day14.txt"), w, h);

//        int w = 11;
//        int h = 7;
//        Day14 day = new Day14(
//                "p=0,4 v=3,-3\n" +
//                "p=6,3 v=-1,-3\n" +
//                "p=10,3 v=-1,2\n" +
//                "p=2,0 v=2,-1\n" +
//                "p=0,0 v=1,3\n" +
//                "p=3,0 v=-2,-2\n" +
//                "p=7,6 v=-1,-3\n" +
//                "p=3,0 v=-1,-2\n" +
//                "p=9,3 v=2,3\n" +
//                "p=7,3 v=-1,2\n" +
//                "p=2,4 v=2,-3\n" +
//                "p=9,5 v=-3,-3",
//                w, h);


        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.getDefaultOfSize(7))
                .setInitialTerminalSize(new TerminalSize(w + 5, h + 5));
        Screen screen = null;


        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            long time = 0;
            day.getRobots().forEach(r -> r.update(7395));
            boolean running = true;
            while (running) {
                KeyStroke keyStroke = screen.pollInput();
                if (keyStroke != null) {
                    if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                        day.getRobots().forEach(r -> r.update(1));
                        time++;
                    } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                        day.getRobots().forEach(r -> r.update(-1));
                        time--;
                    } else if (keyStroke.getKeyType() == KeyType.Escape) {
                        running = false;
                    }
                }

                drawGrid(screen, day, time);
//                day.getRobots().forEach(r -> r.update(1));
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

    private static void drawGrid(Screen screen, Day14 day, long time) throws IOException {
        TextGraphics textGraphics = screen.newTextGraphics();
        screen.clear();

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        for (int y = 0; y < day.h; y++) {
            for (int x = 0; x < day.w; x++) {
                textGraphics.putString(x + 3, y + 3, ".");
            }
        }

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);

//        for (int y = 0; y < day.h; y++) {
//            textGraphics.putString(0, y, String.valueOf(y));
//        }
//
//        for (int x = 0; x < day.w; x++) {
//            textGraphics.putString(x, 0, String.valueOf(x));
//        }

        textGraphics.putString(0, 0, String.valueOf(time));
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        day.robots.forEach(r -> {
            textGraphics.putString(r.p.x + 3, r.p.y + 3, "#");
        });

        screen.refresh();
    }

}


