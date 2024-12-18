package net.woroniecki.aoc2024;

import groovy.util.logging.Slf4j;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Stream;

@lombok.extern.slf4j.Slf4j
@Slf4j

public class Day16 {

    public String toString() {
        return "";
    }

    private final Block[][] grid;

    private Node start, end;

    AStar astar;

    @AllArgsConstructor
    public static class Block {

        int x, y;

        char ch;
    }

    public Day16(String input) {
        String[] lines = input.split("\n");
        grid = new Block[lines.length][];
        for (int y = 0; y < lines.length; y++) {
            char[] chars = lines[y].toCharArray();
            grid[y] = new Block[chars.length];

            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = new Block(x, y, chars[x]);

                if (chars[x] == 'S') {
                    start = new Node(x, y);
                }
                if (chars[x] == 'E') {
                    end = new Node(x, y);
                }
            }
        }

        start.direction = AStar.Direction.RIGHT;
        astar = new AStar(start, end, grid);
    }

    public int part1() {
        AStar path = new AStar(start, end, grid);
        path.complete();
        return path.current.g;
    }

    public int part2() {
        return 0;
    }

    @ToString(of = {"x", "y", "g", "h"})
    static class Node implements Comparable<Node> {

        int x, y;

        int g, h;

        Node parent;

        AStar.Direction direction;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int f() {
            return g + h;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.f(), other.f());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    @Slf4j
    @Getter
    public static class AStar {

        public enum Direction {
            UP(0, -1),
            DOWN(0, 1),
            RIGHT(1, 0),
            LEFT(-1, 0);
            int dx, dy;

            Direction(int dx, int dy) {
                this.dx = dx;
                this.dy = dy;
            }
        }

        private final PriorityQueue<Node> openList = new PriorityQueue<>();
        private final Map<Pair<Integer, Integer>, Node> map = new HashMap<>();

        private final Set<Node> closedList = new HashSet<>();

        private final Node start;

        private final Node end;

        private Node current;

        private final Block[][] grid;

        AStar(Node start, Node end, Block[][] grid) {
            this.start = start;
            this.end = end;
            this.grid = grid;

            this.start.g = 0;
            this.start.h = heuristic(this.start, this.end);
            openList.add(this.start);
            map.put(Pair.with(this.start.x, this.start.y), this.start);
        }

        public void complete() {
            while (!end.equals(current)) {
                step();
            }
        }

        public void step() {
            if (end.equals(current)) return;

            if (!openList.isEmpty()) {
                //log.info("current {}", current);
                current = openList.poll();
                if (current.equals(end)) {
                    //log.info("reached end");
                    return;
                }

                closedList.add(current);

                for (Direction d : Direction.values()) {
                    int newX = current.x + d.dx;
                    int newY = current.y + d.dy;

                    if (isValid(newX, newY)) {
                        Node next;
                        if (map.containsKey(Pair.with(newX, newY))) {
                            next = map.get(Pair.with(newX, newY));
                            //System.out.println("found!");
                        } else {
                            next = new Node(newX, newY);
                        }

                        if (closedList.contains(next)) continue;

                        int tentativeG = current.g + 1 + (current.direction == d ? 0 : 1000);

                        if (tentativeG < next.g) {
                            System.out.println("tentative better!");
                            if (openList.contains(next)) {
                                System.out.println("better and already on list");
                            } else {
                                System.out.println("not yet on list");
                            }
                        }

                        if (tentativeG < next.g || !openList.contains(next)) {
                            next.g = tentativeG;
                            next.h = heuristic(next, end);
                            next.direction = d;
                            next.parent = current;

                            if (!openList.contains(next)) {
                                openList.add(next);
                                map.put(Pair.with(next.x, next.y), next);
                            } else {
                                openList.remove(next);
                                openList.add(next);
                            }
                        }
                    }
                }
            }
        }

        private int heuristic(Node a, Node b) {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
        }

        private boolean isValid(int x, int y) {
            return x >= 0 && y >= 0 && y < grid.length && x < grid[0].length && grid[y][x].ch != '#';
        }

        public List<Node> reconstructPath(Node node) {
            List<Node> path = new ArrayList<>();
            while (node != null) {
                path.add(node);
                node = node.parent;
            }
            Collections.reverse(path);
            return path;
        }
    }

    public Stream<Block> allBlocks() {
        return Arrays.stream(grid).flatMap(Arrays::stream);
    }

}
