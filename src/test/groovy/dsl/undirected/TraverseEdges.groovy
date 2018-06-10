package dsl.undirected

import spock.lang.Specification

import static graph.Graph.graph

class TraverseEdges extends Specification {
    def 'traverseEdges'() {
        given:
        def edges
        graph {
            edge 'A', 'B'
            edge 'C', 'A'
            edges = traverseEdges'A'
        }

        expect:
        edges.find { it.from == 'A' && it.to == 'B' }
        edges.find { it.from == 'C' && it.to == 'A' }
    }
}
