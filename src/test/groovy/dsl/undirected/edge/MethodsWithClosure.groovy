package dsl.undirected.edge

import graph.Graph
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithClosure extends Specification {

    def 'can add edge with closure'() {
        given:
        Graph graph = graph {
            edge([from:'A', to:'B']) {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'B'
    }

    def 'add edge with from and to params and from and to set in map with closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B', [from:'A', to:'B']) {}
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'B'
    }

    def 'changeFrom in closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
                changeFrom 'C'
            }
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

    def 'changeTo in closure'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
                changeTo 'C'
            }
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

    def 'changeFrom by assigning from'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
                from = 'C'
            }
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

    def 'changeTo by assigning to'() {
        given:
        Graph graph = graph {
            edge('A', 'B') {
                to = 'C'
            }
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
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'B'
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
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'B'
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
                prop = getFrom()
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.first().from == 'A'
        graph.edges.first().to == 'B'
        graph.edges.first().prop == 'A'
    }
}
