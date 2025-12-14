package net.woroniecki.aoc2025

import net.woroniecki.DayTemplate
import net.woroniecki.Util
import spock.lang.Specification

class Day04Test extends Specification {

    def "test"() {
        given:
        def input = """
                        ..@@.@@@@.
                        @@@.@.@.@@
                        @@@@@.@.@@
                        @.@@@@..@.
                        @@.@@@@.@@
                        .@@@@@@@.@
                        .@.@.@.@@@
                        @.@@@.@@@@
                        .@@@@@@@@.
                        @.@.@@@.@.
                        """.stripIndent().strip()

        when:
        def day = new Day04(input)

        then:
        day.part1() == 13
        day.part2() == 43
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2025/day04.txt")

        when:
        def day = new Day04(input)

        then:
        day.part1() == 1478
        day.part2() == 9120
    }

}
