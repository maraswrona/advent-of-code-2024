package net.woroniecki.aoc2024

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
                        """.stripIndent().stripIndent()

        expect:
        new Day4(input).part1() == 18
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day4.txt")

        when:
        def day4 = new Day4(input)

        then:
        day4.part1() == 0
        true == false
    }

}
