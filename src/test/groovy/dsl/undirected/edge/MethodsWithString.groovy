package dsl.undirected.edge

import graph.Graph
import static graph.Graph.graph
import spock.lang.Specification

class MethodsWithString extends Specification {
    def 'can add edge'() {
        given:
        Graph graph = graph {
            edge('A', 'B')
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'can add edge with map'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [:])
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
            edge('A', 'B') {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'can add edge with map and closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [:]) {}
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
