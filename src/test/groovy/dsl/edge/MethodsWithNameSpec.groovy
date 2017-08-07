package dsl.edge

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph
import static graph.Graph.graph
import static graph.Graph.graph
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
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
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
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
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
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
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
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'rename one in closure'() {
        given:
        Graph graph = graph {
            edge(C, B) {
                renameOne A
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'rename two in closure'() {
        given:
        Graph graph = graph {
            edge(A, C) {
                renameTwo B
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }
}
