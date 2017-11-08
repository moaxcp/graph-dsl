package dsl.undirected.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

/**
 * These tests cover vertex string methods from within the dsl entry point (static graph method).
 */
class MethodsWithStrings extends Specification {
    def 'add a vertex'() {
        given:
        Graph graph = graph {
            vertex('A')
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add multiple vertices'() {
        given:
        Graph graph = graph {
            vertex('A', 'B', 'C')
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
    }

    def 'add vertex with string and closure'() {
        given:
        Graph graph = graph {
            vertex('A') {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add vertex with string and map'() {
        given:
        Graph graph = graph {
            vertex ('A', [:])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add vertex with string, map, and closure'() {
        given:
        Graph graph = graph {
            vertex ('A', [:]) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }
}
