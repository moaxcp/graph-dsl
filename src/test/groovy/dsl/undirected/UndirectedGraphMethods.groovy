package dsl.undirected

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class UndirectedGraphMethods extends Specification {

    def 'change key'() {
        given:
        Graph graph = graph {
            vertex('A')
            changeId('A', 'B')
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.id == 'B'
    }

    def 'change key null key'() {
        when:
        Graph graph = graph {
            vertex 'A'
            changeId null, 'B'
        }

        then:
        thrown IllegalArgumentException
    }

    def 'change key null new key'() {
        when:
        Graph graph = graph {
            vertex 'A'
            changeId 'A', null
        }

        then:
        thrown IllegalArgumentException
    }
}
