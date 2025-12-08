package net.woroniecki.aoc2025


import net.woroniecki.Util
import spock.lang.Specification

class Day07Test extends Specification {

    def "test"() {
        given:
        def input = """
                        .......S.......
                        ...............
                        .......^.......
                        ...............
                        ......^.^......
                        ...............
                        .....^.^.^.....
                        ...............
                        ....^.^...^....
                        ...............
                        ...^.^...^.^...
                        ...............
                        ..^...^.....^..
                        ...............
                        .^.^.^.^.^...^.
                        ...............
                        """.stripIndent().strip()

        when:
        def day = new Day07(input)

        then:
        day.part1() == 21
        day.part2() == 40
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2025/day07.txt")

        when:
        def day = new Day07(input)

        then:
        day.part1() == 1518
        day.part2() == 25489586715621
    }

}
