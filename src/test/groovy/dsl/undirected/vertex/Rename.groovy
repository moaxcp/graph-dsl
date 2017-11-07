package dsl.undirected.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class Rename extends Specification {

    def 'rename method with String'() {
        given:
        Graph graph = graph {
            vertex('A')
            changeKey('A', 'B')
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def 'rename method with NameSpec'() {
        given:
        Graph graph = graph {
            vertex(A)
            changeKey(A, B)
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def 'rename method null name'() {
        when:
        Graph graph = graph {
            vertex A
            changeKey null, 'B'
        }

        then:
        thrown IllegalArgumentException
    }

    def 'changeKey in map with string'() {
        given:
        Graph graph = graph {
            vertex(A, [changeKey:'B'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def 'changeKey in map with NameSpec'() {
        given:
        Graph graph = graph {
            vertex(A, [changeKey:B])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def 'changeKey in closure with string'() {
        given:
        Graph graph = graph {
            vertex(A) {
                changeKey 'B'
            }
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def 'changeKey in closure with NameSpec'() {
        given:
        Graph graph = graph {
            vertex(A) {
                changeKey B
            }
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.key == 'B'
    }

    def 'changeKey in spec to vertex that already exists'() {
        when:
        Graph graph = graph {
            vertex(A)
            vertex(B)
            vertex(B) {
                changeKey A
            }
        }

        then:
        thrown IllegalStateException
    }
}
