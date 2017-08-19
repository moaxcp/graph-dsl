package graph.type.directed

import graph.type.directed.DirectedEdge
import spock.lang.Specification

public class DirectedEdgeSpec extends Specification {
    def edge = new DirectedEdge(one: 'step1', two: 'step2')

    def 'equals with null'() {
        expect:
        edge != null
    }

    def 'equals with not DirectedEdge'() {
        expect:
        edge != 'hello'
    }

    def 'equals with self'() {
        expect:
        edge.equals(edge)
    }

    def 'equals with both different'() {
        when:
        def edge2 = new DirectedEdge(one: 'step4', two: 'step3')

        then:
        edge != edge2
    }

    def 'equals with first different'() {
        when:
        def edge2 = new DirectedEdge(one: 'step3', two: 'step2')

        then:
        edge.one != edge2.one
        edge.two == edge.two
        edge != edge2
    }

    def 'equals with second different'() {
        when:
        def edge2 = new DirectedEdge(one: 'step1', two: 'step3')

        then:
        edge.one == edge2.one
        edge.two != edge2.two
        edge != edge2
    }

    def 'equals with equal object'() {
        when:
        def edge2 = new DirectedEdge(one: 'step1', two: 'step2')

        then:
        edge.one == edge2.one
        edge.two == edge2.two
        edge == edge2
    }
}
