package net.woroniecki.aoc2024

import spock.lang.Specification

class Day5Test extends Specification {

    def "test"() {
        given:
        def input = """
                        47|53
                        97|13
                        97|61
                        97|47
                        75|29
                        61|13
                        75|53
                        29|13
                        97|29
                        53|29
                        61|53
                        97|53
                        61|29
                        47|13
                        75|47
                        97|75
                        47|61
                        75|61
                        47|29
                        75|13
                        53|13
                        
                        75,47,61,53,29
                        97,61,53,29,13
                        75,29,13
                        75,97,47,61,53
                        61,13,29
                        97,13,75,29,47
                        """.stripIndent().strip()

        when:
        def day5 = new Day5(input)

        then:
        day5.part1() == 143
        day5.part2() == 123
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day5.txt")

        when:
        def day5 = new Day5(input)

        then:
        day5.part1() == 4569
        day5.part2() == 6456
    }

}
