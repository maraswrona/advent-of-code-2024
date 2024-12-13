package net.woroniecki.aoc2024

import net.woroniecki.DayTemplate
import net.woroniecki.Util
import spock.lang.Specification

class Day12Test extends Specification {

    def "test"() {
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
        day.part1() == 140
        //day.part2() == 0
    }

    def "example1"() {
        expect:
            new Day12("A").part1() == 4
            new Day12("AA").part1() == 12
            new Day12("AAA").part1() == 24
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day12.txt")

        when:
        def day = new Day12(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

}
