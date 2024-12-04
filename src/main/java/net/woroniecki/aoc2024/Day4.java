package net.woroniecki.aoc2024;

public class Day4 {

    private static final String WORD = "XMAS";
    private final char[][] table;

    public Day4(String input) {
        String[] lines = input.split("\n");
        table = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            table[i] = lines[i].toCharArray();
        }
    }

    public int part1() {
        int sum = 0;
        for (int i = 0; i < table.length; i++) {
            char[] row = table[i];
            for (int j = 0; j < row.length; j++) {
                char ch = row[j];
                if (ch == WORD.charAt(0)) {
                    sum += countWords(i, j);
                }
            }
        }
        return sum;
    }

    private int countWords(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                count += checkWord(x, y, i, j) ? 1 : 0;
            }
        }
        return count;
    }

    private boolean checkWord(int x, int y, int dirX, int dirY) {
        if (dirX == 0 && dirY == 0) {
            return false;
        }

        for (int i = 0; i < WORD.length(); i++) {
            int x2 = x + dirX * i;
            int y2 = y + dirY * i;
            if (!checkCoords(x2, y2)) {
                return false;
            }

            char charChecked = WORD.charAt(i);
            char charFromTable = table[x2][y2];
            if (charChecked != charFromTable) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCoords(int x, int y) {
        return x >= 0 && x < table.length && y >= 0 && y < table[x].length;
    }

}
