package net.woroniecki.aoc2024


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
        day.findCheats(2, 25) == 4
        day.findCheats(20, 70) == 41
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day20.txt")

        when:
        def day = new Day20(input)

        then:
        day.part1() == 1393
        day.part2() == 990096
    }

}
