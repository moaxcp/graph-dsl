package dsl.undirected

import graph.Edge
import spock.lang.Specification
import static graph.Graph.graph

class AdjacentEdges extends Specification {
    def 'adjacentEdges'() {
        given:
        def edges
        graph {
            edge A, B
            edge C, A
            edges = adjacentEdges('A')
        }

        expect:
        edges.find { it.one == 'A' && it.two == 'B' }
        edges.find { it.one == 'C' && it.two == 'A' }
    }

    def 'adjacentEdges with NameSpec'() {
        given:
        def edges
        graph {
            edge A, B
            edge C, A
            edges = adjacentEdges(A)
        }

        expect:
        edges.find { it.one == 'A' && it.two == 'B' }
        edges.find { it.one == 'C' && it.two == 'A' }
    }
}
