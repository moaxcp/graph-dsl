package dsl.directed

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithConnectsFrom extends Specification {

    def 'create edge using connectsFrom in map'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            vertex ('A', [connectsFrom:'B'])
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.from == 'B' && it.to == 'A' }
    }

    def 'create two edges using connectsFrom in map'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            vertex ('A', [connectsFrom:['B', 'C']])
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.from == 'B' && it.to == 'A' }
        graph.edges.find { it.from == 'C' && it.to == 'A' }
    }

    def 'use connectsFrom'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            vertex ('A') {
                connectsFrom 'B'
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.from == 'B' && it.to == 'A' }
    }

    def 'use connectsFrom with two vertices'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            vertex ('A') {
                connectsFrom 'B', 'C'
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.from == 'B' && it.to == 'A' }
        graph.edges.find { it.from == 'C' && it.to == 'A' }
    }

    def 'use nested connectsFrom'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            vertex ('A') {
                connectsFrom ('B') {
                    connectsFrom ('C')
                }
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.from == 'B' && it.to == 'A' }
        graph.edges.find { it.from == 'C' && it.to == 'B' }
    }

}
