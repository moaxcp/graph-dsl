package dsl.undirected.edge

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class Rename extends Specification {
    def 'renameOne in map'() {
        given:
        Graph graph = graph {
            edge(A, B, [renameOne:'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'renameOne in map with NameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B, [renameOne:C])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'renameOne in closure'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                renameOne 'C'
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'renameOne in closure with NameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                renameOne C
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'created edge then renameOne in map'() {
        given:
        Graph graph = graph {
            edge(A, B)
            edge(A, B, [renameOne:'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'C'
        graph.edges.first().two == 'B'
    }

    def 'can renameTwo in map'() {
        given:
        Graph graph = graph {
            edge(A, B, [renameTwo:'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }

    def 'can renameTwo in map with NameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B, [renameTwo:C])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }

    def 'renameTwo in closure'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                renameTwo 'C'
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }

    def 'renameTwo in closure with NameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                renameTwo C
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }

    def 'created edge then renameTwo in map'() {
        given:
        Graph graph = graph {
            edge(A, B)
            edge(A, B, [renameTwo:'C'])
        }

        expect:
        graph.vertices.size() == 3
        graph.edges.size() == 1
        graph.vertices.A.name == 'A'
        graph.vertices.B.name == 'B'
        graph.vertices.C.name == 'C'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'C'
    }
}
