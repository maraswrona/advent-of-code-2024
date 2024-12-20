package net.woroniecki.aoc2024

import net.woroniecki.DayTemplate
import net.woroniecki.Util
import spock.lang.Specification

class Day20Test extends Specification {

    def "test"() {
        given:
        def input = """
                        ###############
                        #...#...#.....#
                        #.#.#.#.#.###.#
                        #S#...#.#.#...#
                        #######.#.#.###
                        #######.#.#...#
                        #######.#.###.#
                        ###..E#...#...#
                        ###.#######.###
                        #...###...#...#
                        #.#####.#.###.#
                        #.#...#.#.#...#
                        #.#.#.#.#.#.###
                        #...#...#...###
                        ###############
                        """.stripIndent().strip()

        when:
        def day = new Day20(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day20.txt")

        when:
        def day = new Day20(input)

        then:
        //day.part1() == 1393
        day.part2() == 0
    }

}
