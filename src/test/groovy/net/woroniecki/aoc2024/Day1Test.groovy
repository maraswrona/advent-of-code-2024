package net.woroniecki.aoc2024

import spock.lang.Specification

class Day1Test extends Specification {

    def "test"() {
        given:
        def input = """
                    3   4
                    4   3
                    2   5
                    1   3
                    3   9
                    3   3
                    """.stripIndent().strip()

        when:
        def day1 = new Day1(input)

        then:
        day1.distance() == 11
        day1.similarity() == 31
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day1.txt")

        when:
        def day1 = new Day1(input)

        then:
        day1.distance() == 2344935
        day1.similarity() == 27647262
    }

}
