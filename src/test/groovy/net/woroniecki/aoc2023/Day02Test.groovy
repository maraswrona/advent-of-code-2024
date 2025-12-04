package net.woroniecki.aoc2023


import net.woroniecki.Util
import spock.lang.Specification

class Day02Test extends Specification {

    def "test"() {
        given:
        def input = """
                        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
                        """.stripIndent().strip()

        when:
        def day = new Day02(input)

        then:
        day.part1() == 8
        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2023/day2.txt")

        when:
        def day = new Day02(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

}
