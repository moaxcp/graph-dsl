package graph

import spock.lang.Specification

import static graph.Graph.TraversalColor.*
import static graph.EdgeClassification.EdgeType.*

class EdgeClassificationSpec extends Specification {
    def graph = new Graph()
    def edgeClassification = new EdgeClassification()

    def setup() {
        graph.with {
            vertex 'A'
            vertex 'B'
            edge 'A', 'B'
        }
    }

    def 'test from and to in addEdge'() {
        setup:
        def fromCheck
        def toCheck

        when:
        edgeClassification.addEdge(graph, graph.edge('A', 'B'), 'A', 'B', WHITE) { from, to, type ->
            fromCheck = from
            toCheck = to
        }

        then:
        fromCheck == 'A'
        toCheck == 'B'
    }

    def 'test WHITE edge in addEdge'() {
        setup:
        def typeCheck

        when:
        edgeClassification.addEdge(graph, graph.edge('A', 'B'), 'A', 'B', WHITE) { from, to, type ->
            typeCheck = type
        }

        then:
        typeCheck == TREE_EDGE
        edgeClassification.forrest.vertices['A'] == graph.vertex('A')
        edgeClassification.forrest.vertices['B'] == graph.vertex('B')
        edgeClassification.forrest.vertices.size() == 2
        edgeClassification.forrest.edge('A', 'B') == graph.edge('A', 'B')
        edgeClassification.forrest.edges.size() == 1
        edgeClassification.treeEdges.contains(graph.edge('A', 'B'))
    }

    def 'test GREY edge in addEdge'() {
        setup:
        def fromCheck
        def toCheck
        def typeCheck

        when:
        edgeClassification.addEdge(graph, graph.edge('A', 'B'), 'A', 'B', GREY) { from, to, type ->
            fromCheck = from
            toCheck = to
            typeCheck = type
        }

        then:
        fromCheck == 'A'
        toCheck == 'B'
        typeCheck == BACK_EDGE
        edgeClassification.backEdges.contains(graph.edge('A', 'B'))
    }

    def 'test BLACK forward edge in addEdge'() {
        setup:
        def fromCheck
        def toCheck
        def typeCheck

        when:
        edgeClassification.addEdge(graph, graph.edge('A', 'B'), 'A', 'B', BLACK) { from, to, type ->
            fromCheck = from
            toCheck = to
            typeCheck = type
        }

        then:
        fromCheck == 'A'
        toCheck == 'B'
        typeCheck == FORWARD_EDGE
        edgeClassification.forwardEdges.contains(graph.edge('A', 'B'))
    }

    def 'test BLACK cross edge in addEdge'() {
        setup:
        def fromCheck
        def toCheck
        def typeCheck
        graph.vertex('B')
        edgeClassification.forrest.vertex('B')

        when:
        edgeClassification.addEdge(graph, graph.edge('A', 'B'), 'A', 'B', BLACK) { from, to, type ->
            fromCheck = from
            toCheck = to
            typeCheck = type
        }

        then:
        fromCheck == 'A'
        toCheck == 'B'
        typeCheck == CROSS_EDGE
        edgeClassification.crossEdges.contains(graph.edge('A', 'B'))
    }
}
