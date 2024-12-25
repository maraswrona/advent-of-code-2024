package net.woroniecki.aoc2024;

import java.util.ArrayList;
import java.util.List;

public class Day22 {

    private final List<Long> secrets = new ArrayList<>();

    public Day22(String input) {
        String[] split = input.split("\n");
        for (String line : split) {
            secrets.add(Long.valueOf(line.trim()));
        }
    }

    public long part1() {
        return secrets.stream()
                .mapToLong(secret -> nthSecret(secret, 2000))
                .sum();
    }

    public int part2() {
        return 0;
    }

    public long nthSecret(long secret, int n) {
        for (int i = 0; i < n; i++) {
            secret = nextSecret(secret);
        }
        return secret;
    }

    public long nextSecret(long secret) {

        // Calculate the result of multiplying the secret number by 64.
        // Then, mix this result into the secret number.
        // Finally, prune the secret number.
        long a = secret * 64L;
        secret = mix(secret, a);
        secret = prune(secret);

        // Calculate the result of dividing the secret number by 32. Round the result down to the nearest integer.
        // Then, mix this result into the secret number.
        // Finally, prune the secret number.
        long b = secret / 32L;
        secret = mix(secret, b);
        secret = prune(secret);

        // Calculate the result of multiplying the secret number by 2048.
        // Then, mix this result into the secret number.
        // Finally, prune the secret number.
        long c = secret * 2048L;
        secret = mix(secret, c);
        secret = prune(secret);

        return secret;
    }

    public long mix(long secret, long value) {
        // To mix a value into the secret number, calculate the bitwise XOR of the given value and the secret number.
        // Then, the secret number becomes the result of that operation.
        // (If the secret number is 42 and you were to mix 15 into the secret number, the secret number would become 37.)
        return secret ^ value;
    }

    public long prune(long secret) {
        // To prune the secret number, calculate the value of the secret number modulo 16777216.
        // Then, the secret number becomes the result of that operation.
        // (If the secret number is 100000000 and you were to prune the secret number, the secret number would become 16113920.)
        return secret % 16777216;
    }


}
