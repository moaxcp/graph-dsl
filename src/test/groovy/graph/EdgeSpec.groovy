package graph

import spock.lang.Specification

class EdgeSpec extends Specification {
    def edge

    def setup() {
        edge = new Edge(one: 'step1', two: 'step2')
    }
    def 'equals with null'() {
        when:
        def edge2 = new Edge()

        then:
        edge2 != null
    }

    def 'equals with both different'() {
        when:
        def edge2 = new Edge(one: 'step4', two: 'step3')

        then:
        edge != edge2
    }

    def 'equals with first different'() {
        when:
        def edge2 = new Edge(one: 'step3', two: 'step2')

        then:
        edge != edge2
    }

    def 'equals with second different'() {
        when:
        def edge2 = new Edge(one: 'step1', two: 'step3')

        then:
        edge != edge2
    }
}
