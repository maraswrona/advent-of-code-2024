package net.woroniecki.aoc2024

import net.woroniecki.Util
import spock.lang.Specification

class Day08Test extends Specification {

    def "test"() {
        given:
        def input = """
                        ............
                        ........0...
                        .....0......
                        .......0....
                        ....0.......
                        ......A.....
                        ............
                        ............
                        ........A...
                        .........A..
                        ............
                        ............
                        """.stripIndent().strip()

        when:
        def day = new Day08(input)

        then:
        day.part1() == 14
        day.part2() == 34
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day8.txt")

        when:
        def day = new Day08(input)

        then:
        day.part1() == 247
        day.part2() == 861
    }

}
