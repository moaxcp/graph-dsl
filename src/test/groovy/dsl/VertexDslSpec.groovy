package dsl

import graph.Graph
import spock.lang.Specification
import static graph.Graph.graph

class VertexDslSpec extends Specification {
    def 'add a vertex with a string'() {
        given:
        Graph graph = graph {
            vertex 'A'
        }

        expect:
        graph.vertices.size() == 1
        graph.vertex('A').name == 'A'
    }

    def 'add a vertex with a VertexNameSpec'() {
        given:
        Graph graph = graph {
            vertex A
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }
}
