package dsl

import graph.Graph
import graph.type.directed.DirectedGraphType
import spock.lang.Specification

import static graph.Graph.graph

class ConnectsToConnectsFromSpec extends Specification {

    def 'use connectsFrom with a VertexNameSpec'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex A {
                connectsFrom B
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
    }

    def 'use connectsFrom with two VertexNameSpec'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex A {
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

    def 'use connectsTo with a VertexSpec'() {
        given:
        Graph graph = graph {
            vertex A {
                connectsTo B {}
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
    }

    def 'use nested connectsTo with a VertexSpec'() {
        given:
        Graph graph = graph {
            vertex A {
                connectsTo B {
                    connectsTo C
                }
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'B' && it.two == 'C' }
    }

    def 'use connectsFrom with a VertexSpec'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex A {
                connectsFrom B {}
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
    }

    def 'use nested connectsFrom with a VertexSpec'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
            vertex A {
                connectsFrom B {
                    connectsFrom C
                }
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.find { it.one == 'B' && it.two == 'A' }
        graph.edges.find { it.one == 'C' && it.two == 'B' }
    }
}
