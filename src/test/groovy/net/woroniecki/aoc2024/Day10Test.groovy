package net.woroniecki.aoc2024

import net.woroniecki.Util
import spock.lang.Specification

class Day10Test extends Specification {

    def "test"() {
        given:
        def input = """
                        89010123
                        78121874
                        87430965
                        96549874
                        45678903
                        32019012
                        01329801
                        10456732
                        """.stripIndent().strip()

        when:
        def day = new Day10(input)

        then:
        day.part1() == 36
        day.part2() == 81
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day10.txt")

        when:
        def day = new Day10(input)

        then:
        day.part1() == 531
        day.part2() == 1210
    }

}
