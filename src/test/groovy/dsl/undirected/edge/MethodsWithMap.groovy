package dsl.undirected.edge

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithMap extends Specification {
    def 'can add edge'() {
        given:
        Graph graph = graph {
            edge([one:'A', two:'B'])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'can add edge with closure'() {
        given:
        Graph graph = graph {
            edge([one:'A', two:'B']) {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'add edge with one and two params and one and two set in map'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [one:'A', two:'B'])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'add edge with one and two params and one and two set in map with closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [one:'A', two:'B']) {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }
}
