package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day16Test extends Specification {

    def "example 1"() {
        given:
        def input = """
                        ###############
                        #.......#....E#
                        #.#.###.#.###.#
                        #.....#.#...#.#
                        #.###.#####.#.#
                        #.#.#.......#.#
                        #.#.#####.###.#
                        #...........#.#
                        ###.#.#####.#.#
                        #...#.....#.#.#
                        #.#.#.###.#.#.#
                        #.....#...#.#.#
                        #.###.#.#.#.#.#
                        #S..#.....#...#
                        ###############
                        """.stripIndent().strip()

        when:
        def day = new Day16(input)

        then:
        day.part1() == 7036
        //day.part2() == 0
    }

    def "example 2"() {
        given:
        def input = """
                        #################
                        #...#...#...#..E#
                        #.#.#.#.#.#.#.#.#
                        #.#.#.#...#...#.#
                        #.#.#.#.###.#.#.#
                        #...#.#.#.....#.#
                        #.#.#.#.#.#####.#
                        #.#...#.#.#.....#
                        #.#.#####.#.###.#
                        #.#.#.......#...#
                        #.#.###.#####.###
                        #.#.#...#.....#.#
                        #.#.#.#####.###.#
                        #.#.#.........#.#
                        #.#.#.#########.#
                        #S#.............#
                        #################
                        """.stripIndent().strip()

        when:
        def day = new Day16(input)

        then:
        day.part1() == 11048
//        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day16.txt")

        when:
        def day = new Day16(input)

        then:
        day.part1() == 0 // 102508 wrong?
        day.part2() == 0
    }

}
