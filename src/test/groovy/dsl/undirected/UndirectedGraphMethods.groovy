package dsl.undirected

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class UndirectedGraphMethods extends Specification {

    def 'change key'() {
        given:
        Graph graph = graph {
            vertex('A')
            changeKey('A', 'B')
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def 'change key null key'() {
        when:
        Graph graph = graph {
            vertex 'A'
            changeKey null, 'B'
        }

        then:
        thrown IllegalArgumentException
    }

    def 'change key null new key'() {
        when:
        Graph graph = graph {
            vertex 'A'
            changeKey 'A', null
        }

        then:
        thrown IllegalArgumentException
    }
}
