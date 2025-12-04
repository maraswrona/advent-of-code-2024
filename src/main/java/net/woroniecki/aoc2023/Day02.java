package net.woroniecki.aoc2023;

import lombok.Value;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Map;

public class Day02 {

    Map<String, Game> games;

    @Value
    private static class Game {

        List<Draw> draws;

        // 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        static Game from(String s) {
            return new Game(StreamEx.of(s.split(";"))
                    .map(String::trim)
                    .map(Draw::from)
                    .toList());
        }

    }

    @Value
    private static class Draw {
        Map<String, Integer> map;

        // 5 blue, 4 red, 13 green;
        static Draw from(String s) {
            return new Draw(StreamEx.of(s.split(","))
                    .map(String::trim)
                    .map(ss -> ss.split(" "))
                    .toMap(ss -> ss[1].trim(), ss -> Integer.parseInt(ss[0].trim())));
        }

        boolean isPossible(Map<String, Integer> config) {
            return EntryStream.of(map)
                    .noneMatch(e -> e.getValue() > config.get(e.getKey()));
        }
    }

    public Day02(String input) {
        games = StreamEx.of(input.split("\n"))
                .toMap(g -> {
                    String[] split = g.split(": ");
                    return split[0];
                }, g -> {
                    String[] split = g.split(": ");
                    return Game.from(split[1]);
                });
    }

    public int part1() {
        Map<String, Integer> config = Map.of("red", 12, "green", 13, "blue", 14);

        return EntryStream.of(games)
                .filter(game -> game.getValue().draws.stream().allMatch(d -> d.isPossible(config)))
                .map(game -> game.getKey().split(" ")[1])
                .mapToInt(Integer::parseInt)
                .sum();

    }

    public int part2() {
        return 0;
    }


}
