package net.woroniecki.aoc2023


import net.woroniecki.Util
import spock.lang.Specification

class Day1Test extends Specification {

    def "test part 1"() {
        given:
        def input = """
                        1abc2
                        pqr3stu8vwx
                        a1b2c3d4e5f
                        treb7uchet
                        """.stripIndent().strip()

        when:
        def day = new Day1(input)

        then:
        day.part1() == 142
    }

    def "test part 2"() {
        given:
        def input = """
                        two1nine
                        eightwothree
                        abcone2threexyz
                        xtwone3four
                        4nineeightseven2
                        zoneight234
                        7pqrstsixteen
                        """.stripIndent().strip()

        when:
        def day = new Day1(input)

        then:
        day.part2() == 281
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2023/day1.txt")

        when:
        def day = new Day1(input)

        then:
        day.part1() == 55090
        day.part2() == 0
    }

}
