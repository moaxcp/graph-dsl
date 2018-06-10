package dsl.undirected

import graph.Edge
import spock.lang.Specification
import static graph.Graph.graph

class AdjacentEdges extends Specification {
    def 'adjacentEdges'() {
        given:
        def edges
        graph {
            edge 'A', 'B'
            edge 'C', 'A'
            edges = adjacentEdges('A')
        }

        expect:
        edges.find { it.from == 'A' && it.to == 'B' }
        edges.find { it.from == 'C' && it.to == 'A' }
    }
}
