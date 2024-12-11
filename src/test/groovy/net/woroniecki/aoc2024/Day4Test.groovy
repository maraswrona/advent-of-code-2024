package net.woroniecki.aoc2024

import net.woroniecki.Util
import spock.lang.Specification

class Day4Test extends Specification {

    def "test"() {
        given:
        def input = """
                        MMMSXXMASM
                        MSAMXMSMSA
                        AMXSXMAAMM
                        MSAMASMSMX
                        XMASAMXAMM
                        XXAMMXXAMA
                        SMSMSASXSS
                        SAXAMASAAA
                        MAMMMXMMMM
                        MXMXAXMASX
                        """.stripIndent().strip()

        expect:
        new Day4(input).countXmas() == 18
        new Day4(input).countCrossMas() == 9
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day4.txt")

        when:
        def day4 = new Day4(input)

        then:
        day4.countXmas() == 2493
        day4.countCrossMas() == 1890
    }

}
