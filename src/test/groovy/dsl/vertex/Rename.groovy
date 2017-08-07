package dsl.vertex

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class Rename extends Specification {

    def 'rename method with String'() {
        given:
        Graph graph = graph {
            vertex('A')
            rename('A', 'B')
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.name == 'B'
    }

    def 'rename method with NameSpec'() {
        given:
        Graph graph = graph {
            vertex(A)
            rename(A, B)
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.name == 'B'
    }

    def 'rename in map with string'() {
        given:
        Graph graph = graph {
            vertex(A, [rename:'B'])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.name == 'B'
    }

    def 'rename in map with NameSpec'() {
        given:
        Graph graph = graph {
            vertex(A, [rename:B])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.name == 'B'
    }

    def 'rename in closure with string'() {
        given:
        Graph graph = graph {
            vertex(A) {
                rename 'B'
            }
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.name == 'B'
    }

    def 'rename in closure with NameSpec'() {
        given:
        Graph graph = graph {
            vertex(A) {
                rename B
            }
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.name == 'B'
    }
}
