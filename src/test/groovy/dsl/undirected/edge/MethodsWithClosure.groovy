package dsl.undirected.edge

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithClosure extends Specification {

    def 'can add edge with closure'() {
        given:
        Graph graph = graph {
            edge([one:'A', two:'B']) {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'add edge with one and two params and one and two set in map with closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [one:'A', two:'B']) {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
    }

    def 'changeOne in closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
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

    def 'changeTwo in closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
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

    def 'changeOne by assigning one'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
                one = 'C'
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

    def 'changeTwo by assigning two'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
                two = 'C'
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

    def 'add property in closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
                prop = 'value'
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
        graph.edges.first().prop == 'value'
    }

    def 'property missing in closure returns property from edge'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [missing:'value']) {
                prop = missing
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
        graph.edges.first().prop == 'value'
    }

    def 'property missing in closure and edge will throw MissingPropertyException'() {
        when:
        Graph graph = graph {
            edge('A', 'B') {
                prop = missing
            }
        }

        then:
        thrown MissingPropertyException
    }

    def 'method missing in closure executes on edge'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
                prop = getOne()
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.key == 'A'
        graph.vertices.B.key == 'B'
        graph.edges.first().one == 'A'
        graph.edges.first().two == 'B'
        graph.edges.first().prop == 'A'
    }
}
