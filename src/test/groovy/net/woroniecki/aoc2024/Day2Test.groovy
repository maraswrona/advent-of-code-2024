package net.woroniecki.aoc2024

import net.woroniecki.Util
import spock.lang.Specification

class Day2Test extends Specification {

    def "test"() {
        given:
        def input = """
                    7 6 4 2 1
                    1 2 7 8 9
                    9 7 6 2 1
                    1 3 2 4 5
                    8 6 4 4 1
                    1 3 6 7 9
                    """.stripIndent().strip()

        when:
        def day2 = new Day2(input)

        then:
        day2.safe1() == 2
        day2.safe2() == 4
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day2.txt")

        when:
        def day2 = new Day2(input)

        then:
        day2.safe1() == 564
        day2.safe2() == 604
    }

}
