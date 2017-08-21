package dsl

import graph.Graph
import graph.type.directed.DirectedGraphType
import spock.lang.Specification

class TypeMethods extends Specification {

    def 'can change type of Graph with Class'() {
        given:
        Graph graph = new Graph()

        when:
        graph.type(DirectedGraphType)

        then:
        graph.types.contains(DirectedGraphType)
    }

    def 'can change type of Graph with String'() {
        given:
        Graph graph = new Graph()

        when:
        graph.type('directed-graph')

        then:
        graph.types.contains(DirectedGraphType)
    }
}
