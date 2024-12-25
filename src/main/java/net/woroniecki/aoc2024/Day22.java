package net.woroniecki.aoc2024;

import lombok.AllArgsConstructor;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 {

    private final List<Long> secrets = new ArrayList<>();
    final List<Buyer> buyers = new ArrayList<>();

    @AllArgsConstructor
    static class Buyer {
        List<Long> secrets;
        List<Integer> prices;
        List<Integer> changes;

        Map<String, Integer> combinations;
    }

    public Day22(String input) {
        int n = 2000;
        String[] split = input.split("\n");
        for (String line : split) {
            secrets.add(Long.valueOf(line.trim()));
        }

        secrets.forEach(secret -> {

            List<Long> secrets = new ArrayList<>(n+1);
            secrets.add(secret);

            List<Integer> prices = new ArrayList<>(n+1);
            int price = (int) (secret % 10L);
            prices.add(price);

            List<Integer> changes = new ArrayList<>(n+1);

            Map<String, Integer> combinations = new HashMap<>();

            for (int i = 0; i < n; i++) {
                secret = nextSecret(secret);
                secrets.add(secret);

                price = (int) (secret % 10L);
                prices.add(price);

                int change = price - prices.get(i);
                changes.add(change);

                if (i >= 3) {
                    int ch1 = changes.get(i - 3);
                    int ch2 = changes.get(i - 2);
                    int ch3 = changes.get(i - 1);
                    int ch4 = changes.get(i);

                    String key = ch1 + "|" + ch2 + "|" + ch3 + "|" + ch4;
                    if (!combinations.containsKey(key)) {
                        combinations.put(key, price);
                    }
                }
            }

            Buyer buyer = new Buyer(secrets, prices, changes, combinations);
            buyers.add(buyer);
        });
    }

    public long part1() {
        return secrets.stream()
                .mapToLong(secret -> nthSecret(secret, 2000))
                .sum();
    }

    public int part2() {

        Map<String, Integer> totals = new HashMap<>();

        for (Buyer buyer : buyers) {
            buyer.combinations.forEach((key, price) -> {
                if(totals.containsKey(key)) {
                    totals.put(key, totals.get(key) + price);
                } else {
                    totals.put(key, price);
                }
            });
        }

        return EntryStream.of(totals)
                .maxBy(Map.Entry::getValue)
                .map(Map.Entry::getValue)
                .orElseThrow();

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
