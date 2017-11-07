package dsl.undirected.edge

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithNameSpec extends Specification {
    def 'can add edge'() {
        given:
        Graph graph = graph {
            edge(A, B)
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'can add edge with map'() {
        given:
        Graph graph = graph {
            edge(A, B, [:])
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
            edge(A, B) {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'can add edge with map and closure'() {
        given:
        Graph graph = graph {
            edge(A, B, [:]) {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'change one in closure'() {
        given:
        Graph graph = graph {
            edge(C, B) {
                changeOne A
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'change two in closure'() {
        given:
        Graph graph = graph {
            edge(A, C) {
                changeTwo B
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }
}
