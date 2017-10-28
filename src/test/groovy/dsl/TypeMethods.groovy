package dsl

import graph.Graph
import static graph.Graph.graph
import graph.type.directed.DirectedGraphType
import spock.lang.Specification

class TypeMethods extends Specification {

    def 'can change type of Graph with Class'() {
        given:
        Graph graph = graph {
            type DirectedGraphType
        }

        expect:
        graph.type instanceof DirectedGraphType
    }

    def 'can change type of Graph with String'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
        }

        expect:
        graph.type instanceof DirectedGraphType
    }

    def 'type must use Type interface'() {
        when:
        Graph graph = graph {
            type String
        }

        then:
        thrown IllegalArgumentException
    }
}
