package net.woroniecki.aoc2024


import spock.lang.Specification

class Day11Test extends Specification {

    static {
        List<Long>.metaClass.encode = { -> delegate.collect { it -> String.valueOf(it) }.join(" ") }
    }

    def "test"() {
        given:
        def day = new Day11("125 17")

        expect:
        day.blink(6).size() == 22
        day.blink(25).size() == 55312
    }

    def "example1"() {
        given:
        def day = new Day11("0 1 10 99 999")

        expect:
        day.blink(blinks).encode() == stones

        where:
        blinks | stones
        1      | "1 2024 1 0 9 9 2021976"
        2      | "2024 20 24 2024 1 18216 18216 4092479424"
        3      | "20 24 2 0 2 4 20 24 2024 36869184 36869184 40924 79424"
        4      | "2 0 2 4 4048 1 4048 8096 2 0 2 4 20 24 3686 9184 3686 9184 82830176 160754176"
        5      | "4048 1 4048 8096 40 48 2024 40 48 80 96 4048 1 4048 8096 2 0 2 4 36 86 91 84 36 86 91 84 8283 176 325366452224"
        6      | "40 48 2024 40 48 80 96 4 0 4 8 20 24 4 0 4 8 8 0 9 6 40 48 2024 40 48 80 96 4048 1 4048 8096 3 6 8 6 9 1 8 4 3 6 8 6 9 1 8 4 82 83 356224 325366 452224"
    }

    def "example2"() {
        given:
        def day = new Day11("125 17")

        expect:
        day.blink(blinks).encode() == stones

        where:
        blinks | stones
        1      | "253000 1 7"
        2      | "253 0 2024 14168"
        3      | "512072 1 20 24 28676032"
        4      | "512 72 2024 2 0 2 4 2867 6032"
        5      | "1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32"
        6      | "2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2"
    }

    def "example3"() {
        given:
        def day = new Day11("0")

        expect:
        day.blink(blinks).encode() == stones

        where:
        blinks | stones
        1      | "1"
        2      | "2024"
        3      | "20 24"
        4      | "2 0 2 4"
        5      | "4048 1 4048 8096"
        6      | "40 48 2024 40 48 80 96"
    }

    def "puzzle part1"() {
        expect:
        new Day11("92 0 286041 8034 34394 795 8 2051489").part1() == 239714
    }

    def "puzzle part2"() {
        expect:
        new Day11("92 0 286041 8034 34394 795 8 2051489").part2() == 284973560658514
    }

}
