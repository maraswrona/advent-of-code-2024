package net.woroniecki.aoc2024

import net.woroniecki.DayTemplate
import net.woroniecki.Util
import spock.lang.Specification

class Day23Test extends Specification {

    def "test"() {
        given:
        def input = """
                        kh-tc
                        qp-kh
                        de-cg
                        ka-co
                        yn-aq
                        qp-ub
                        cg-tb
                        vc-aq
                        tb-ka
                        wh-tc
                        yn-cg
                        kh-ub
                        ta-co
                        de-co
                        tc-td
                        tb-wq
                        wh-td
                        ta-ka
                        td-qp
                        aq-cg
                        wq-ub
                        ub-vc
                        de-ta
                        wq-aq
                        wq-vc
                        wh-yn
                        ka-de
                        kh-ta
                        co-tc
                        wh-qp
                        tb-vc
                        td-yn
                        """.stripIndent().strip()

        when:
        def day = new Day23(input)

        then:
        day.part1() == 7
        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day.txt")

        when:
        def day = new Day23(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

}
