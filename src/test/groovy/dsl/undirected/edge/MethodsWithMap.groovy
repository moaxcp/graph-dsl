package dsl.undirected.edge

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithMap extends Specification {
    def 'can add edge'() {
        given:
        Graph graph = graph {
            edge([from:'A', to:'B'])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'B'
    }

    def 'add edge with from and to params and from and to set in map'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [from:'A', to:'B'])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'B'
    }
    def 'changeFrom in map'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [changeFrom:'C'])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.first().from == 'C'
        graph.edges.first().to == 'B'
    }

    def 'created edge then changeFrom in map'() {
        given:
        Graph graph = graph {
            edge('A', 'B')
            edge('A', 'B', [changeFrom:'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.first().from == 'C'
        graph.edges.first().to == 'B'
    }

    def 'can changeTo in map'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [changeTo:'C'])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.C.id == 'C'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'C'
    }

    def 'created edge then changeTo in map'() {
        given:
        Graph graph = graph {
            edge('A', 'B')
            edge('A', 'B', [changeTo:'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'C'
    }

    def 'create edge and change new edge to overlap'() {
        when:
        Graph graph = graph {
            edge('A', 'B')
            edge('A', 'C', [changeTo:'B'])
        }

        then:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'B'
    }

    def 'create edge and change existing edge to overlap'() {
        when:
        Graph graph = graph {
            edge('A', 'B')
            edge('A', 'C')
            edge('A', 'C', [changeTo:'B'])
        }

        then:
        thrown IllegalStateException
    }
}
