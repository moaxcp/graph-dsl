package dsl

import graph.Edge
import spock.lang.Specification
import static graph.Graph.graph

class GraphWithNumbers extends Specification {
    def 'create vertex with number'() {
        given:
        def graph = graph {
            vertex 1
        }

        expect:
        graph.vertices[1].id == 1
    }

    def 'create edge with number'() {
        given:
        def graph = graph {
            edge 1, 2
        }

        expect:
        graph.vertices[1].id == 1
        graph.vertices[2].id == 2
        graph.edges.first() == new Edge(one:1, two:2)
    }
}
