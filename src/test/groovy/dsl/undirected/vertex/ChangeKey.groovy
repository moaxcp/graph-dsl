package dsl.undirected.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class ChangeKey extends Specification {

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

    def 'changeKey in closure'() {
        given:
        Graph graph = graph {
            vertex('A') {
                changeKey 'B'
            }
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def 'changeKey in closure with null'() {
        when:
        Graph graph = graph {
            vertex('A') {
                changeKey null
            }
        }

        then:
        thrown IllegalArgumentException
    }

    def 'changeKey in spec to vertex that already exists'() {
        when:
        Graph graph = graph {
            vertex('A')
            vertex('B')
            vertex('B') {
                changeKey 'A'
            }
        }

        then:
        thrown IllegalStateException
    }
}
