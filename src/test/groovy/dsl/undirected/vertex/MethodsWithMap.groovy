package dsl.undirected.vertex

import graph.Graph
import graph.trait.Mapping
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithMap extends Specification {
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

    def 'add with name param and name set in map'() {
        given:
        Graph graph = graph {
            vertex('A', [name:'A'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }

    def 'add with name param and name set in map with closure'() {
        given:
        Graph graph = graph {
            vertex('A', [name:'A']) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }
}
