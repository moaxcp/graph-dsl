package dsl.undirected.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithConnectsTo extends Specification {
    def 'create edge using connectsTo in map with NameSpec'() {
        given:
        Graph graph = graph {
            vertex A (connectsTo:B)
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
    }

    def 'create edge using connectsTo in map with string'() {
        given:
        Graph graph = graph {
            vertex A (connectsTo:'B')
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
    }
    def 'create two edges using connectsTo in map with NameSpec'() {
        given:
        Graph graph = graph {
            vertex A (connectsTo:[B, C])
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'A' && it.two == 'C' }
    }

    def 'create two edges using connectsTo in map with string'() {
        given:
        Graph graph = graph {
            vertex A (connectsTo:['B', 'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'A' && it.two == 'C' }
    }

    def 'create edge using connectsTo in closure with NameSpec'() {
        given:
        Graph graph = graph {
            vertex(A) {
                connectsTo B
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
    }

    def 'create edge using connectsTo in closure with string'() {
        given:
        Graph graph = graph {
            vertex(A) {
                connectsTo 'B'
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
    }
    def 'create two edges using connectsTo in closure with NameSpec'() {
        given:
        Graph graph = graph {
            vertex(A) {
                connectsTo B, C
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'A' && it.two == 'C' }
    }

    def 'create two edges using connectsTo in closure with string'() {
        given:
        Graph graph = graph {
            vertex(A) {
                connectsTo 'B', 'C'
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'A' && it.two == 'C' }
    }
}
