package net.woroniecki.aoc2024;

public class Day04 {

    private final char[][] table;

    public Day04(String input) {
        String[] lines = input.split("\n");
        table = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            table[i] = lines[i].toCharArray();
        }
    }

    public int countXmas() {
        int sum = 0;
        for (int i = 0; i < table.length; i++) {
            char[] row = table[i];
            for (int j = 0; j < row.length; j++) {
                char ch = row[j];
                if (ch == 'X') {
                    sum += countWordsAround("XMAS", i, j);
                }
            }
        }
        return sum;
    }

    public int countCrossMas() {
        int sum = 0;
        for (int i = 0; i < table.length; i++) {
            char[] row = table[i];
            for (int j = 0; j < row.length; j++) {
                char ch = row[j];
                if (ch == 'A') {
                    sum += checkWordsCrossing("MAS", i, j) ? 1 : 0;
                }
            }
        }
        return sum;
    }

    private boolean checkWordsCrossing(String word, int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            if (i == 0) continue;
            for (int j = -1; j <= 1; j++) {
                if (j == 0) continue;

                count += checkWord(word, x + i, y + j, -i, -j) ? 1 : 0;
            }
        }
        return count == 2;
    }

    private int countWordsAround(String word, int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                count += checkWord(word, x, y, i, j) ? 1 : 0;
            }
        }
        return count;
    }

    private boolean checkWord(String word, int x, int y, int dirX, int dirY) {
        if (dirX == 0 && dirY == 0) {
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            int x2 = x + dirX * i;
            int y2 = y + dirY * i;
            if (!checkCoords(x2, y2)) {
                return false;
            }

            char charChecked = word.charAt(i);
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
