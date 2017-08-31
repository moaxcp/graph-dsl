package dsl.directed

import graph.type.directed.DirectedGraphType
import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithConnectsFrom extends Specification {
    def 'create edge using connectsFrom in map with NameSpec'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex A (connectsFrom:B)
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
    }

    def 'create edge using connectsFrom in map with string'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex A (connectsFrom:'B')
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
    }
    def 'create two edges using connectsFrom in map with NameSpec'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex A (connectsFrom:[B, C])
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
        graph.edges.find { it.one == 'C' && it.two == 'A' }
    }

    def 'create two edges using connectsFrom in map with string'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex A (connectsFrom:['B', 'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
        graph.edges.find { it.one == 'C' && it.two == 'A' }
    }

    def 'create edge using connectsFrom in closure with NameSpec'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex(A) {
                connectsFrom B
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
    }

    def 'create edge using connectsFrom in closure with string'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex(A) {
                connectsFrom 'B'
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
    }
    def 'create two edges using connectsFrom in closure with NameSpec'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex(A) {
                connectsFrom B, C
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
        graph.edges.find { it.one == 'C' && it.two == 'A' }
    }

    def 'create two edges using connectsFrom in closure with string'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex(A) {
                connectsFrom 'B', 'C'
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
        graph.edges.find { it.one == 'C' && it.two == 'A' }
    }
}
