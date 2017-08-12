package graph.type.directed

import graph.Graph
import graph.type.directed.DirectedEdge
import graph.type.directed.DirectedGraphPlugin
import spock.lang.Specification

class DirectedGraphEdgeSpec extends Specification {

    def graph = new Graph()

    def setup() {
        graph.apply DirectedGraphPlugin
    }

    def 'can get DirectedEdge from newEdge'() {
        when:
        def edge = graph.edgeFactory.newEdge 'step1', 'step2'

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
        graph.apply DirectedGraphPlugin

        then:
        graph.edges.every {
            it instanceof DirectedEdge
        }
    }

    def 'can add edges after apply'() {
        setup:
        def graph = new Graph()
        graph.edge 'step1', 'step2'

        when:
        graph.apply DirectedGraphPlugin

        and:
        graph.edge 'step2', 'step3'

        then:
        graph.edges.any {
            it.one == 'step2' && it.two == 'step3'
        }
    }
}
