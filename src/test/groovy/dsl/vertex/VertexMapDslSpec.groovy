package dsl.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class VertexMapDslSpec extends Specification {
    def 'add a vertex with Map'() {
        given:
        Graph graph = graph {
            vertex([name:'A'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }

    def 'add a vertex with Map and closure'() {
        given:
        Graph graph = graph {
            vertex([name:'A']) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }
}
