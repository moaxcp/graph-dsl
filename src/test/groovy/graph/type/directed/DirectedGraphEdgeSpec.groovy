package graph.type.directed

import graph.Graph
import spock.lang.Specification

class DirectedGraphEdgeSpec extends Specification {

    def graph = new Graph()

    def setup() {
        graph.type DirectedGraphType
    }

    def 'can get DirectedEdge from newEdge'() {
        when:
        def edge = graph.newEdge one:'step1', two:'step2'

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
        graph.type DirectedGraphType

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
        graph.type DirectedGraphType

        and:
        graph.edge 'step2', 'step3'

        then:
        graph.edges.any {
            it.one == 'step2' && it.two == 'step3'
        }
    }
}
