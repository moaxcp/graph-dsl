package dsl.undirected.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithMap extends Specification {
    def 'add a vertex with Map'() {
        given:
        Graph graph = graph {
            vertex([id:'A'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'add a vertex with Map and closure'() {
        given:
        Graph graph = graph {
            vertex([id:'A']) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'add with id param and id set in map'() {
        given:
        Graph graph = graph {
            vertex('A', [id:'A'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'add with id param and id set in map with closure'() {
        given:
        Graph graph = graph {
            vertex('A', [id:'A']) {}
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'change id in map'() {
        given:
        Graph graph = graph {
            vertex('A', [changeId:'B'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.id == 'B'
    }

    def 'change id in map null'() {
        when:
        Graph graph = graph {
            vertex('A', [changeId:null])
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
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.from == 'A' && it.to == 'B' }
    }

    def 'create two edges using connectsTo in map'() {
        given:
        Graph graph = graph {
            vertex('A', [connectsTo:['B', 'C']])
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.from == 'A' && it.to == 'B' }
        graph.edges.find { it.from == 'A' && it.to == 'C' }
    }
}
