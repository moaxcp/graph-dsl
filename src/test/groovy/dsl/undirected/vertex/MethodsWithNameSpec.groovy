package dsl.undirected.vertex

import graph.Graph
import spock.lang.Specification
import static graph.Graph.graph

/**
 * These tests cover vertex NameSpec methods from within the dsl entry point (static graph method).
 */
class MethodsWithNameSpec extends Specification {

    def 'add a vertex with a NameSpec'() {
        given:
        Graph graph = graph {
            vertex(A)
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }

    def 'add multiple vertices'() {
        given:
        Graph graph = graph {
            vertex(A, B, C)
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
    }

    def 'add a vertex with a NameSpec and Closure as method'() {
        given:
        Graph graph = graph {
            vertex (A) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }

    def 'add a vertex with a NameSpec and Map as method'() {
        given:
        Graph graph = graph {
            vertex (A, [:])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }

    def 'add a vertex with a NameSpec, Map, and Closure as method'() {
        given:
        Graph graph = graph {
            vertex (A, [:]) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
    }
}
