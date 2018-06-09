package dsl.directed

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithConnectsTo extends Specification {

    def 'create edge using connectsTo in map'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            vertex('A', [connectsTo:'B'])
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
    }

    def 'create two edges using connectsTo in map'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            vertex('A', [connectsTo:['B', 'C']])
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'A' && it.two == 'C' }
    }

    def 'use connectsTo in closure'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            vertex ('A') {
                connectsTo 'B'
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
    }

    def 'use connectsTo in closure with two vertices'() {
        given:
        Graph graph = graph {
            vertex ('A') {
                type 'directed-graph'
                connectsTo 'B', 'C'
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'A' && it.two == 'C' }
    }

    def 'use nested connectsTo in closure'() {
        given:
        Graph graph = graph {
            vertex ('A') {
                type 'directed-graph'
                connectsTo ('B') {
                    connectsTo 'C'
                }
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'B' && it.two == 'C' }
    }
}
