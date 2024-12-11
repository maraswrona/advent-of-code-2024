package net.woroniecki.aoc2024

import net.woroniecki.Util
import spock.lang.Specification

class Day6Test extends Specification {

    def "test"() {
        given:
        def input = """
                        ....#.....
                        .........#
                        ..........
                        ..#.......
                        .......#..
                        ..........
                        .#..^.....
                        ........#.
                        #.........
                        ......#...
                        """.stripIndent().strip()

        when:
        def day = new Day6(input)

        then:
        day.part1() == 41
        day.part2() == 6
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day6.txt")

        when:
        def day = new Day6(input)

        then:
        day.part1() == 5404
        day.part2() == 1984
    }

}
