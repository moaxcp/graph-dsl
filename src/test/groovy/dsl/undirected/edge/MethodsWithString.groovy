package dsl.undirected.edge

import graph.Graph
import static graph.Graph.graph
import spock.lang.Specification

class MethodsWithString extends Specification {
    def 'can add edge'() {
        given:
        Graph graph = graph {
            edge('A', 'B')
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'can add edge with map'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [:])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'can add edge with closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'can add edge with map and closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [:]) {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'cannot add edge when one is null'() {
        when:
        Graph graph = graph {
            edge(null, 'B')
        }

        then:
        thrown IllegalArgumentException
    }

    def 'cannot add edge when two is null'() {
        when:
        Graph graph = graph {
            edge('A', null)
        }

        then:
        thrown IllegalArgumentException
    }
}
