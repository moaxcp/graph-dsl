package graph

import spock.lang.Specification

public class DirectedEdgeSpec extends Specification {
    def edge = new Edge(one: 'step1', two: 'step2') as DirectedEdge

    def 'equals with null'() {
        when:
        def edge2 = new Edge(one: 'step1', two: 'step2') as DirectedEdge

        then:
        edge2 != null
    }

    def 'equals with both different'() {
        when:
        def edge2 = new Edge(one: 'step4', two: 'step3') as DirectedEdge

        then:
        edge != edge2
    }

    def 'equals with first different'() {
        when:
        def edge2 = new Edge(one: 'step3', two: 'step2') as DirectedEdge

        then:
        edge != edge2
    }

    def 'equals with second different'() {
        when:
        def edge2 = new Edge(one: 'step1', two: 'step3') as DirectedEdge

        then:
        edge != edge2
    }
}
