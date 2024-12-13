package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day12Test extends Specification {

    def "example 1"() {
        given:
        def input = """
                        AAAA
                        BBCD
                        BBCC
                        EEEC
                        """.stripIndent().strip()

        when:
        def day = new Day12(input)

        then:
        with(day.region('A' as char)) {
            area == 4
            perimeter == 10
            sides == 4
        }

        with(day.region('B' as char)) {
            area == 4
            perimeter == 8
            sides == 4
        }

        with(day.region('C' as char)) {
            area == 4
            perimeter == 10
            sides == 8
        }

        with(day.region('D' as char)) {
            area == 1
            perimeter == 4
            sides == 4
        }

        with(day.region('E' as char)) {
            area == 3
            perimeter == 8
            sides == 4
        }

        and:
        day.part1() == 140
        day.part2() == 80
    }

    def "example 2"() {
        given:
        def input = """
                        OOOOO
                        OXOXO
                        OOOOO
                        OXOXO
                        OOOOO
                        """.stripIndent().strip()

        when:
        def day = new Day12(input)

        then:
        with(day.region('O' as char)) {
            area == 21
            perimeter == 36
        }

        day.part1() == 772
    }

    def "example 3"() {
        given:
        def input = """
                        RRRRIICCFF
                        RRRRIICCCF
                        VVRRRCCFFF
                        VVRCCCJFFF
                        VVVVCJJCFE
                        VVIVCCJJEE
                        VVIIICJJEE
                        MIIIIIJJEE
                        MIIISIJEEE
                        MMMISSJEEE
                        """.stripIndent().strip()

        expect:
        def day = new Day12(input).part1() == 1930
    }

    def "example 4"() {
        given:
        def input = """
                        EEEEE
                        EXXXX
                        EEEEE
                        EXXXX
                        EEEEE
                        """.stripIndent().strip()

        when:
        def day = new Day12(input)

        then:
        with(day.region('E' as char)) {
            area == 17
            sides == 12
        }

        day.part2() == 236
    }

    def "example 5"() {
        expect:
        new Day12("A").part1() == 4
        new Day12("AA").part1() == 12
        new Day12("AAA").part1() == 24

        new Day12("A").part2() == 4
        new Day12("AA").part2() == 8
        new Day12("AAA").part2() == 12
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day12.txt")

        when:
        def day = new Day12(input)

        then:
        day.part1() == 1363484
        day.part2() == 838988
    }

}
