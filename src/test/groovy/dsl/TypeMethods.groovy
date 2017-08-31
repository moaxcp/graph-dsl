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
        graph.types.contains(DirectedGraphType)
    }

    def 'can change type of Graph with String'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
        }

        expect:
        graph.types.contains(DirectedGraphType)
    }
}
