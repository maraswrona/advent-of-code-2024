package net.woroniecki.aoc2024

import net.woroniecki.DayTemplate
import net.woroniecki.Util
import spock.lang.Specification

class Day21Test extends Specification {

    def "test"() {
        given:
        def input = """
                        029A
                        980A
                        179A
                        456A
                        379A
                        """.stripIndent().strip()

        when:
        def day = new Day21(input)

        then:
        day.part1() == 126384
        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = """
                        319A
                        085A
                        143A
                        286A
                        789A
                        """.stripIndent().strip()

        when:
        def day = new Day21(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

}
