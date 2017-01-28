package graph

import spock.lang.Specification

class DirectedGraphEdgeSpec extends Specification {

    def graph = new Graph()

    def setup() {
        graph.apply DirectedGraph
    }

    def 'can get DirectedEdge from newEdge'() {
        when:
        def edge = graph.newEdge 'step1', 'step2'

        then:
        edge instanceof DirectedEdge
    }

    def 'can get DirectedEdge from edge'() {
        when:
        def edge = graph.edge 'step1', 'step2'

        then:
        edge instanceof DirectedEdge
    }

    def 'old edges become DirectedEdge'() {
        setup:
        def graph = new Graph()
        graph.edge 'step1', 'step2'

        when:
        graph.apply DirectedGraph

        then:
        graph.edges.every {
            it instanceof DirectedEdge
        }
    }
}
