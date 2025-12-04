package net.woroniecki.aoc2025;

public class Day04 {

    char[][] grid;

    public Day04(String input) {

        String[] lines = input.split("\n");
        grid = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            grid[i] = lines[i].trim().toCharArray();
        }
    }

    public int part1() {

        int n = grid.length;
        int m = grid[0].length;

        int available = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (grid[r][c] == '@') {
                    int nbrs = neighbors(r, c);
                    if (nbrs < 4) {
                        available++;
                    }
                }
            }
        }

        return available;
    }

    private int neighbors(int r, int c) {

        int[][] MOVES = new int[][]{
                new int[]{-1, -1},
                new int[]{-1, +0},
                new int[]{-1, +1},

                new int[]{+1, -1},
                new int[]{+1, +0},
                new int[]{+1, +1},

                new int[]{0, -1},
                new int[]{0, +1},
        };


        int nbrs = 0;
        for (int i = 0; i < MOVES.length; i++) {

            int rr = r + MOVES[i][0];
            int cc = c + MOVES[i][1];

            if (coordsValid(rr, cc)) {
                if (grid[rr][cc] == '@') {
                    nbrs++;
                }
            }

        }

        return nbrs;
    }

    boolean coordsValid(int r, int c) {
        int n = grid.length;
        int m = grid[0].length;

        return 0 <= r && r < n && 0 <= c && c < m;
    }

    public int part2() {
        int n = grid.length;
        int m = grid[0].length;

        int available = 0;
        boolean modified = false;
        do {
            modified = false;
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < m; c++) {
                    if (grid[r][c] == '@') {
                        int nbrs = neighbors(r, c);
                        if (nbrs < 4) {
                            available++;
                            grid[r][c] = '.';
                            modified = true;
                        }
                    }
                }
            }
        } while (modified);

        return available;
    }


}
