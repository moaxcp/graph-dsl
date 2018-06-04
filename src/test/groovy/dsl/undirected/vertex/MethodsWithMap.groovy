package dsl.undirected.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithMap extends Specification {
    def 'add a vertex with Map'() {
        given:
        Graph graph = graph {
            vertex([key:'A'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add a vertex with Map and closure'() {
        given:
        Graph graph = graph {
            vertex([key:'A']) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add with key param and key set in map'() {
        given:
        Graph graph = graph {
            vertex('A', [key:'A'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'add with key param and key set in map with closure'() {
        given:
        Graph graph = graph {
            vertex('A', [key:'A']) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.key == 'A'
    }

    def 'change key in map'() {
        given:
        Graph graph = graph {
            vertex('A', [changeKey:'B'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def 'change key in map null'() {
        when:
        Graph graph = graph {
            vertex('A', [changeKey:null])
        }

        then:
        thrown IllegalArgumentException
    }

    def 'create edge using connectsTo in map'() {
        given:
        Graph graph = graph {
            vertex('A', [connectsTo:'B'])
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
    }

    def 'create two edges using connectsTo in map'() {
        given:
        Graph graph = graph {
            vertex('A', [connectsTo:['B', 'C']])
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'A' && it.two == 'C' }
    }
}
