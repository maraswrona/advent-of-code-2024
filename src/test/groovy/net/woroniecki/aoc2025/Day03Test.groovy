package net.woroniecki.aoc2025


import net.woroniecki.Util
import spock.lang.Specification

class Day03Test extends Specification {

    def "test"() {
        given:
        def input = """
                        987654321111111
                        811111111111119
                        234234234234278
                        818181911112111
                        """.stripIndent().strip()

        when:
        def day = new Day03(input)

        then:
        day.part1() == 357
        day.part2() == 3121910778619
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2025/day03.txt")

        when:
        def day = new Day03(input)

        then:
        day.part1() == 17346
        day.part2() == 172981362045136
    }

}
