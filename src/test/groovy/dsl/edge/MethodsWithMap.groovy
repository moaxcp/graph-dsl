package dsl.edge

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
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
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
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }
}
