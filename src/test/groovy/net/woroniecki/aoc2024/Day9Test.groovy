package net.woroniecki.aoc2024

import spock.lang.Specification

class Day9Test extends Specification {

    def "test"() {
        given:
        def input = """
                        2333133121414131402
                        """.stripIndent().strip()

        when:
        def day = new Day9(input)

        then:
        day.part1() == 1928
        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day9.txt")

        when:
        def day = new DayTemplate(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

}
