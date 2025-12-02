package net.woroniecki.aoc2025

import net.woroniecki.Util
import spock.lang.Specification

class Day01Test extends Specification {
    def "test"() {
        given:
        def input = """
                    L68
                    L30
                    R48
                    L5
                    R60
                    L55
                    L1
                    L99
                    R14
                    L82
                    """.stripIndent().strip()

        when:
        def day1 = new Day01(input)

        then:
        day1.part1() == 3
        day1.part2() == 6
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2025/day01.txt")

        when:
        def day1 = new Day01(input)

        then:
        day1.part1() == 999
        day1.part2() == 6099
    }
}
