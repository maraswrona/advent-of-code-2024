package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day22Test extends Specification {

    def "example"() {
        given:
        def input = """
                        1
                        10
                        100
                        2024
                        """.stripIndent().strip()

        when:
        def day = new Day22(input)

        then:
        day.part1() == 37327623
        //day.part2() == 0
    }

    def "test"() {
        given:
        def day = new Day22("123")

        expect:
        day.nthSecret(123, 1) == 15887950
        day.nthSecret(123, 2) == 16495136
        day.nthSecret(123, 3) == 527345
        day.nthSecret(123, 4) == 704524
        day.nthSecret(123, 5) == 1553684
        day.nthSecret(123, 6) == 12683156
        day.nthSecret(123, 7) == 11100544
        day.nthSecret(123, 8) == 12249484
        day.nthSecret(123, 9) == 7753432
        day.nthSecret(123, 10) == 5908254

        day.nthSecret(1, 2000) == 8685429
        day.nthSecret(10, 2000) == 4700978
        day.nthSecret(100, 2000) == 15273692
        day.nthSecret(2024, 2000) == 8667524
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day22.txt")

        when:
        def day = new Day22(input)

        then:
        day.part1() == 15303617151
        //day.part2() == 0
    }

}
