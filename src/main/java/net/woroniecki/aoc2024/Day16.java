package net.woroniecki.aoc2024;

import groovy.util.logging.Slf4j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@lombok.extern.slf4j.Slf4j
@Slf4j
public class Day16 {

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
                    grid[y][x] = new Block(x, y, chars[x]);
                }
                if (chars[x] == 'E') {
                    end = new Node(x, y);
                    grid[y][x] = new Block(x, y, chars[x]);
                }
            }
        }

        astar = new AStar(start, end, grid);
    }

    public int part1() {
        List<Node> path = new AStar(start, end, grid).reconstructPath(end);
        System.out.println(path);
        return 0;
    }

    public int part2() {
        return 0;
    }

    @ToString(of = { "x", "y", "g", "h" })
    static class Node implements Comparable<Node> {

        int x, y;

        int g, h; // Koszty: g (start->tu), h (tu->cel)

        Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int f() {
            return g + h; // Łączny koszt
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

        private final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // Ruchy: góra, prawo, dół, lewo

        private final PriorityQueue<Node> openList = new PriorityQueue<>();

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
        }

        public void step() {
            if (end.equals(current)) return;

            if (!openList.isEmpty()) {
                log.info("current {}", current);
                current = openList.poll();
                if (current.equals(end)) {
                    log.info("reached end");
                    return;
                }

                closedList.add(current);

                for (int[] direction : DIRECTIONS) {
                    int newX = current.x + direction[0];
                    int newY = current.y + direction[1];

                    if (isValid(newX, newY)) {
                        Node neighbor = new Node(newX, newY);
                        if (closedList.contains(neighbor)) continue;

                        int tentativeG = current.g + 1; // Koszt przejścia (1 na kratkę)
                        if (tentativeG < neighbor.g || !openList.contains(neighbor)) {
                            neighbor.g = tentativeG;
                            neighbor.h = heuristic(neighbor, end);
                            neighbor.parent = current;

                            if (!openList.contains(neighbor)) {
                                openList.add(neighbor);
                            }
                        }
                    }
                }
            }
        }

        private int heuristic(Node a, Node b) {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Heurystyka Manhattan
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
