package graph

import spock.lang.Specification

class EdgeSpec extends Specification {
    def edge = new Edge(one: 'step1', two: 'step2')

    def 'equals with null'() {
        when:
        def edge2 = new Edge()

        then:
        !edge2.equals(null)
    }

    def 'equals with self'() {
        when:
        def edge2 = new Edge()

        then:
        edge2.equals(edge2)
    }

    def 'equals with second edge'() {
        when:
        def edge2 = new Edge(one: 'step1', two: 'step2')

        then:
        edge == edge2
    }

    def 'equals with both vertices different'() {
        when:
        def edge2 = new Edge(one: 'step4', two: 'step3')

        then:
        edge != edge2
        edge2 != edge
    }

    def 'equals with first vertex different'() {
        when:
        def edge2 = new Edge(one: 'step3', two: 'step2')

        then:
        edge != edge2
        edge2 != edge
    }

    def 'equals with second vertex different'() {
        when:
        def edge2 = new Edge(one: 'step1', two: 'step3')

        then:
        edge != edge2
        edge2 != edge
    }

    def 'equals with both vertices switched'() {
        when:
        def edge2 = new Edge(one: 'step1', two: 'step2')

        then:
        edge == edge2
    }
}
