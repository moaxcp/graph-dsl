package dsl.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class VertexConfigSpecDslSpec extends Specification {

    def 'add a vertex with a ConfigSpec with Closure'() {
        given:
        Graph graph = graph {
            vertex A {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }

    def 'add a vertex with a ConfigSpec with Map and Closure'() {
        given:
        Graph graph = graph {
            vertex A([:]) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }
}
