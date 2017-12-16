package graph.type.undirected

import graph.Graph
import static graph.Graph.graph
import spock.lang.Specification

class GraphTypeSpec extends Specification {

    GraphType type

    def setup() {
        type = new GraphType()
    }

    def 'is not multigraph'() {
        expect:
        !type.isMultiGraph()
    }

    def 'is directed'() {
        expect:
        !type.isDirected()
    }

    def 'is not weighted'() {
        expect:
        !type.isWeighted()
    }

    def 'canConvert is false with two edges between vertices'() {
        given: 'directed graph with edge(A, B) and edge(B, A)'
        Graph graph = graph {
            type 'directed-graph'
            edge A, B
            edge B, A
        }

        and: 'a GraphType for the graph'
        type.graph = graph

        expect: 'graph cannot be converted'
        !type.canConvert()
    }

    def 'cannot convert graph with two edges between vertices'() {
        given: 'directed graph with two edges between vertices'
        Graph graph = graph {
            type 'directed-graph'
            edge A, B
            edge B, A
        }

        and: 'a GraphType for the graph'
        type.graph = graph

        when:'the GraphType attempts to convert the graph'
        type.convert()

        then: 'IllegalArgumentException is thrown'
        IllegalArgumentException ex = thrown()
        ex.message == 'Cannot convert to GraphType'
    }

    def 'traverse edges returns adjacent edges'() {
        given: 'graph with vertex that has adjacent edges'
        Graph graph = graph {
            edge A, B
            edge A, C
            edge A, D
        }

        and: 'a GraphType for the graph'
        type.graph = graph

        expect: 'traverse edges returns adjacent edges'
        graph.adjacentEdges('A') == type.traverseEdges('A')
    }
}
