package net.woroniecki.aoc2024;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import lombok.Value;
import one.util.streamex.StreamEx;

import java.io.IOException;
import java.util.*;

public class Day21Vis2 {

    public static void main(String[] args) throws IOException {


        Day21.PinPad pinpad = new Day21.PinPad(
                "789\n" +
                        "456\n" +
                        "123\n" +
                        " 0A"
        );

        String pin = "379A";
        String firstMoves = pinpad.movesToTypeSequence(pin);

        Solver solver = new Solver(pin, Lists.charactersOf(firstMoves), 25);

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.getDefaultOfSize(18))
                .setInitialTerminalSize(new TerminalSize(100, 35));
        Screen screen = null;

        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            boolean running = true;
            boolean auto = false;

            while (running) {
                KeyStroke keyStroke = screen.pollInput();
                if (keyStroke != null) {
                    switch (keyStroke.getKeyType()) {
                        case ArrowRight -> {
                        }
                        case ArrowLeft -> {
                        }
                        case ArrowUp -> {
                        }
                        case ArrowDown -> {
                        }

                        case Character -> {
                            if (keyStroke.getCharacter() == ' ') {
                                solver.step();
                            }
                        }
                        case Enter -> {
                            auto = !auto;
                        }
                        case Backspace -> {

                        }
                        case Escape -> running = false;
                        case null, default -> {
                        }
                    }
                }

                if (auto) {
                    for (int i = 0; i < 2_000_000; i++) {
                        solver.step();
                    }
                }


                drawGrid(screen, solver);
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

    private static void drawGrid(Screen screen, Solver solver) throws IOException {
        TextGraphics textGraphics = screen.newTextGraphics();
        screen.clear();

        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.putString(1, 1, solver.pin);

        int y = 3;
        for (Solver.MovesProcessor processor : solver.processors) {
            String processed = String.valueOf(processor.processed);
            String moves = StreamEx.of(processor.moves).joining();
            String lastMove = String.valueOf(processor.lastMove);
            textGraphics.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
            textGraphics.putString(1, y, processed);

            textGraphics.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
            textGraphics.putString(18, y, lastMove);

            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.putString(20, y, moves);
            y += 1;
        }

        textGraphics.setForegroundColor(TextColor.ANSI.RED);
        String totalMoves = String.valueOf(solver.totalMoves);
        textGraphics.putString(1, y + 1, totalMoves);

        Deque<Solver.CacheKey> cacheStack = solver.cacheStack;
        textGraphics.putString(40, 1, "Cache stack size: " + cacheStack.size());

        Map<Solver.CacheKey, Solver.CacheValue> cache = solver.cache;
        textGraphics.putString(40, 3, "Cache size: " + cache.size());

        Map<Solver.CacheKey, Solver.CacheValue> cacheInProgress = solver.cacheInProgress;
        textGraphics.putString(40, 5, "Cache in progress size: " + cacheInProgress.size());



        screen.refresh();






    }

    public static class Solver {

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
                .put("vA", ">^A")

                .put(">^", "<^A")
                .put(">v", "<A")
                .put(">A", "^A")
                .build();

        final List<MovesProcessor> processors = new ArrayList<>();
        final String pin;
        long totalMoves;

        Map<CacheKey, CacheValue> cache = new HashMap<>();
        Map<CacheKey, CacheValue> cacheInProgress = new HashMap<>();
        Deque<CacheKey> cacheStack = new LinkedList<>();
        boolean caching = false;

        public Solver(String pin, List<Character> firstMoves, int nprocessors) {
            this.pin = pin;

            for (int i = 0; i < nprocessors; i++) {
                processors.add(new MovesProcessor());
            }

            processors.get(0).moves.addAll(firstMoves);
        }

        @Value
        static class CacheKey {
            int processor;
            char state;
            char move;
        }

        @Value
        static class CacheValue {
            List<Long> movesGenerated;
            long totalMovesGenerated;
        }

        static class MovesProcessor {
            Deque<Character> moves = new LinkedList<>();
            Character lastMove = 'A';
            long processed = 0;
        }

        public void step() {
            // find last non empty queue
            int queueIndex = -1;
            for (int i = processors.size() - 1; i >= 0; i--) {
                if (!processors.get(i).moves.isEmpty()) {
                    queueIndex = i;
                    //log.info("Non empty queue is {} and contains: {}", queueIndex, processors.get(i).moves);
                    break;
                }
            }

            if (queueIndex < 0) {
                // no non empty queue found, all processed
                //log.info("No more queues to process - ending");
                return;
            }

            if (caching && !cacheStack.isEmpty()) {
                CacheKey keyToClose = cacheStack.removeLast();
                CacheValue precacheSnapshot = cacheInProgress.get(keyToClose);

                List<Long> newMovesGenerated = new ArrayList<>();
                for (int i = 0; i < precacheSnapshot.movesGenerated.size(); i++) {
                    long moves = processors.get(i + keyToClose.processor).processed - precacheSnapshot.movesGenerated.get(i);
                    newMovesGenerated.add(moves);
                }

                CacheValue valueToCache = new CacheValue(newMovesGenerated, totalMoves - precacheSnapshot.totalMovesGenerated);
                cache.put(keyToClose, valueToCache);
                cacheInProgress.remove(keyToClose);
            }


            MovesProcessor currentProcessor = processors.get(queueIndex);
            char nextMove = currentProcessor.moves.poll();

            // caching
            List<Character> processorsState = new ArrayList<>();
            for (int i = queueIndex; i < processors.size(); i++) {
                processorsState.add(processors.get(i).lastMove);
            }
            CacheKey key = new CacheKey(queueIndex, currentProcessor.lastMove, nextMove);


            if (caching) {
                if (cache.containsKey(key)) {
                    //log.info("Cache hit");
                    CacheValue value = cache.get(key);

                    currentProcessor.lastMove = nextMove;
                    for (int i = 0; i < value.movesGenerated.size(); i++) {
                        processors.get(i + queueIndex).processed += value.movesGenerated.get(i);
                    }
                    totalMoves += value.totalMovesGenerated;
                    return;
                } else {
                    cacheStack.add(key);
                    List<Long> processed = new ArrayList<>();
                    long total = totalMoves;
                    for (int i = queueIndex; i < processors.size(); i++) {
                        processed.add(processors.get(i).processed);
                    }
                    CacheValue value = new CacheValue(processed, total);
                    cacheInProgress.put(key, value);
                }
            }


            final ImmutableList<Character> stepsForNextProcessor;
            if (currentProcessor.lastMove == nextMove) {
                stepsForNextProcessor = ImmutableList.of('A');
            } else {
                String currentStep = "" + currentProcessor.lastMove + nextMove;
                stepsForNextProcessor = Lists.charactersOf(MOVES_LOOKUP.get(currentStep));
            }
            //log.info("Moves are {}", moves);
            if (queueIndex < processors.size() - 1) {
                //log.info("Adding to next processor");
                MovesProcessor nextProcessor = processors.get(queueIndex + 1);
                nextProcessor.moves.addAll(stepsForNextProcessor);
            } else {
                //log.info("Last processor reached - adding to result");
                totalMoves += stepsForNextProcessor.size();
            }

            //log.info("Last move for that processor is: {}", move);
            currentProcessor.processed++;
            currentProcessor.lastMove = nextMove;

        }
    }


}


