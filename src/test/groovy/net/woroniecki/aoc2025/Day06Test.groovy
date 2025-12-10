package net.woroniecki.aoc2025


import net.woroniecki.Util
import spock.lang.Specification

class Day06Test extends Specification {

    def "test"() {
        given:
        def input = """
                        123 328  51 64 
                         45 64  387 23 
                          6 98  215 314
                        *   +   *   +  
                        """.stripIndent().strip()

        when:
        def day = new Day06(input)

        then:
        day.part1() == 4277556
        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2025/day06.txt")

        when:
        def day = new Day06(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

}
