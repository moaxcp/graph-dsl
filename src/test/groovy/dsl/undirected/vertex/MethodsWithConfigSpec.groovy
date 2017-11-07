package dsl.undirected.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithConfigSpec extends Specification {

    def 'add a vertex with a ConfigSpec with Closure'() {
        given:
        Graph graph = graph {
            vertex A {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add a vertex with a ConfigSpec with Map and Closure'() {
        given:
        Graph graph = graph {
            vertex A([:]) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def ' create using name and map with map containing name'() {
        given:
        Graph graph = graph {
            vertex A(key:'B')
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def ' create using name, map, and closure with map containing name'() {
        given:
        Graph graph = graph {
            vertex A(key:'B') {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }
}
