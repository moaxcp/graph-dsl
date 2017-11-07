package dsl.undirected.edge

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class Rename extends Specification {
    def 'changeOne in map'() {
        given:
        Graph graph = graph {
            edge(A, B, [changeOne:'C'])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'changeOne in map with NameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B, [changeOne:C])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'changeOne in closure'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                changeOne 'C'
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'changeOne in closure with NameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                changeOne C
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'created edge then changeOne in map'() {
        given:
        Graph graph = graph {
            edge(A, B)
            edge(A, B, [changeOne:'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'can changeTwo in map'() {
        given:
        Graph graph = graph {
            edge(A, B, [changeTwo:'C'])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }

    def 'can changeTwo in map with NameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B, [changeTwo:C])
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }

    def 'changeTwo in closure'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                changeTwo 'C'
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }

    def 'changeTwo in closure with NameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                changeTwo C
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }

    def 'created edge then changeTwo in map'() {
        given:
        Graph graph = graph {
            edge(A, B)
            edge(A, B, [changeTwo:'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.vertices.C.key == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }

    def 'create edge and change new edge to overlap'() {
        when:
        Graph graph = graph {
            edge(A, B)
            edge(A, C, [changeTwo:'B'])
        }

        then:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'create edge and change existing edge to overlap'() {
        when:
        Graph graph = graph {
            edge(A, B)
            edge(A, C)
            edge(A, C, [changeTwo:'B'])
        }

        then:
        thrown IllegalStateException
    }
}
