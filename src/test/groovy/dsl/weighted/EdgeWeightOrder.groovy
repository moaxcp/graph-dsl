package dsl.weighted

import graph.Graph
import spock.lang.Specification
import static graph.Graph.graph

class EdgeWeightOrder extends Specification {
    def 'missing weight is ok'() {
        given:
        Graph graph = graph {
            type 'weighted-graph'
            edge A, B
            edge C, A
        }

        when:
        def edges = graph.adjacentEdges('A')

        then:
        graph.edges.size() == 2
        edges.size() == 2
        edges.find { it.one == 'A' && it.two == 'B' }
        edges.find { it.one == 'C' && it.two == 'A' }
    }
    def 'null weight is ok'() {
        given:
        Graph graph = graph {
            type 'weighted-graph'
            edge (A, B) { weight = null }
            edge (C, A) { weight = null }
        }

        when:
        def edges = graph.adjacentEdges('A')

        then:
        graph.edges.size() == 2
        edges.size() == 2
        edges.find { it.one == 'A' && it.two == 'B' }
        edges.find { it.one == 'C' && it.two == 'A' }
    }

    def 'adding weight of zero is ok'() {
        given:
        Graph graph = graph {
            type 'weighted-graph'
            edge (A, B) { weight = 0 }
            edge (C, A) { weight = 0 }
        }

        when:
        def edges = graph.adjacentEdges('A')

        then:
        graph.edges.size() == 2
        edges.size() == 2
        edges.find { it.one == 'A' && it.two == 'B' }
        edges.find { it.one == 'C' && it.two == 'A' }
    }

    def 'weight is equal and one is equal'() {
        given:
        Graph graph = graph {
            type 'weighted-graph'
            edge (A, B) { weight = 0 }
            edge (A, C) { weight = 0 }
        }

        when:
        def edges = graph.adjacentEdges('A')

        then:
        graph.edges.size() == 2
        edges.size() == 2
        edges.find { it.one == 'A' && it.two == 'B' }
        edges.find { it.one == 'A' && it.two == 'C' }
    }

    def 'traverseEdges sorts edges by weight'() {
        given:
        Graph graph = graph {
            type 'weighted-graph'
            edge (A, B) { weight = 3 }
            edge (A, C) { weight = 1 }
            edge (A, D) { weight = 2 }
        }

        when:
        def edges = graph.traverseEdges('A')

        then:
        edges[0].two == 'C'
        edges[1].two == 'D'
        edges[2].two == 'B'
    }

    def 'traverseEdges sorts edges by weight after type set'() {
        given:
        Graph graph = graph {
            edge (A, B) { weight = 3 }
            edge (A, C) { weight = 1 }
            edge (A, D) { weight = 2 }
            type 'weighted-graph'
        }

        when:
        def edges = graph.traverseEdges('A')

        then:
        edges[0].two == 'C'
        edges[1].two == 'D'
        edges[2].two == 'B'
    }
}
