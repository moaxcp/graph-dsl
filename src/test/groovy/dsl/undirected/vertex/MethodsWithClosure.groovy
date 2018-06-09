package dsl.undirected.vertex

import graph.Graph
import static graph.Graph.graph
import spock.lang.Specification

class MethodsWithClosure extends Specification {

    def 'use connectsTo in closure'() {
        given:
        Graph graph = graph {
            vertex ('A') {
                connectsTo 'B'
            }
        }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
    }

    def 'use connectsTo in closure with two vertices'() {
        given:
        Graph graph = graph {
            vertex ('A') {
                connectsTo 'B', 'C'
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'A' && it.two == 'C' }
    }

    def 'use nested connectsTo in closure'() {
        given:
        Graph graph = graph {
            vertex ('A') {
                connectsTo ('B') {
                    connectsTo 'C'
                }
            }
        }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.one == 'A' && it.two == 'B' }
        graph.edges.find { it.one == 'B' && it.two == 'C' }
    }

    def 'nested connectsTo in closure with multiple letters'() {
        given:
        Graph graph = graph {
            vertex('first') {
                connectsTo('second') {

                }
            }
        }

        expect:
        graph.vertices.size() == 2
    }

    def 'changeId in closure'() {
        given:
        Graph graph = graph {
            vertex('A') {
                changeId 'B'
            }
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.id == 'B'
    }

    def 'changeId in closure with null'() {
        when:
        Graph graph = graph {
            vertex('A') {
                changeId null
            }
        }

        then:
        thrown IllegalArgumentException
    }

    def 'changeId in spec to vertex that already exists'() {
        when:
        Graph graph = graph {
            vertex('A')
            vertex('B')
            vertex('B') {
                changeId 'A'
            }
        }

        then:
        thrown IllegalStateException
    }

    def 'changeId by assigning key'() {
        when:
        Graph graph = graph {
            vertex('A') {
                id = 'B'
            }
        }

        then:
        graph.vertices.size() == 1
        graph.vertices.B.id == 'B'
    }

    def 'add property in closure'() {
        given:
        Graph graph = graph {
            vertex('A') {
                prop = 'value'
            }
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.prop == 'value'
    }

    def 'property missing in closure returns property from vertex'() {
        given:
        Graph graph = graph {
            vertex('A', [missing:'value']) {
                prop = missing
            }
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.prop == 'value'
    }

    def 'property missing in closure and vertex will throw MissingPropertyException'() {
        when:
        Graph graph = graph {
            vertex('A') {
                prop = missing
            }
        }

        then:
        thrown MissingPropertyException
    }

    def 'method missing in closure executes on vertex'() {
        given:
        Graph graph = graph {
            vertex('A') {
                prop = getId()
            }
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.prop == 'A'
    }
}
