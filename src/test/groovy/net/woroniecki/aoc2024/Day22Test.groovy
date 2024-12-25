package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day22Test extends Specification {

    def "example part 1"() {
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
    }

    def "example part 2"() {
        given:
        def input = """
                        1
                        2
                        3
                        2024
                        """.stripIndent().strip()

        when:
        def day = new Day22(input)

        then:
        day.part2() == 23
    }

    def "test secret calculation"() {
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

    def "test part 2 prices calculation"() {
        given:
        def day = new Day22("123")
        Day22.Buyer buyer = day.buyers.get(0)

        expect:
        buyer.prices.subList(0, 10) == [3, 0, 6, 5, 4, 4, 6, 4, 4, 2]
        buyer.changes.subList(0, 9) == [-3, 6, -1, -1, 0, 2, -2, 0, -2]


    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day22.txt")

        when:
        def day = new Day22(input)

        then:
        day.part1() == 15303617151
        day.part2() == 1727
    }

}
