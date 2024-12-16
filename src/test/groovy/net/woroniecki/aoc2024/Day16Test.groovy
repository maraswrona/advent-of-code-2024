package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day16Test extends Specification {

    def "test"() {
        given:
        def input = """
                        
                        """.stripIndent().strip()

        when:
        def day = new Day16(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day16.txt")

        when:
        def day = new Day16(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

}