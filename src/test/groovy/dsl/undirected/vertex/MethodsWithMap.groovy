package dsl.undirected.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithMap extends Specification {
    def 'add a vertex with Map'() {
        given:
        Graph graph = graph {
            vertex([key:'A'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add a vertex with Map and closure'() {
        given:
        Graph graph = graph {
            vertex([key:'A']) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add with key param and key set in map'() {
        given:
        Graph graph = graph {
            vertex('A', [key:'A'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add with key param and key set in map with closure'() {
        given:
        Graph graph = graph {
            vertex('A', [key:'A']) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }
}
