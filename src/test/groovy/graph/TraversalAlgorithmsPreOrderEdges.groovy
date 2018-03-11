package graph

import spock.lang.Specification

import static graph.Graph.graph
import static graph.TraversalAlgorithms.preOrderEdgesTraversal
import static graph.TraversalColor.*
import static graph.TraversalState.CONTINUE
import static graph.TraversalState.STOP

class TraversalAlgorithmsPreOrderEdges extends Specification {

    Graph graph = graph {}

    def 'pre-order-edges null spec'() {
        when: 'pre-order-edges is called with a null spec'
        preOrderEdgesTraversal(graph, null, null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'pre-order-edges empty spec'() {
        when: 'pre-order-edges is called with an empty spec'
        preOrderEdgesTraversal(graph, [:], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'pre-order-edges missing colors'() {
        when: 'pre-order-edges is called with missing colors'
        preOrderEdgesTraversal(graph, [root:'A'], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'pre-order-edges missing action'() {
        when: 'pre-order-edges is called with missing action'
        Map result = preOrderEdgesTraversal(graph, [root:'A', colors:[:]], null)

        then: 'invalid results are returned'
        result == [root:'A', colors:[A:BLACK], state:CONTINUE]
    }

    def 'pre-order-edges one edge with missing action'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when: 'pre-order-edges is called with missing action'
        preOrderEdgesTraversal(graph, [root:'A', colors:[:]], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'pre-order-edges empty graph with valid action'() {
        when: 'pre-order-edges is called with empty graph and valid action'
        def actionNotCalled = true
        preOrderEdgesTraversal(graph, [root:'A', colors:[:]]) {
            actionNotCalled = false
            CONTINUE
        }

        then: 'action is not called'
        actionNotCalled
    }

    def 'pre-order-edges returns spec' () {
        given: 'spec param'
        Map spec = [root:'A', colors:[:]]

        when: 'pre-order-edges is called'
        Map result = preOrderEdgesTraversal(graph, spec) { }

        then: 'result is original spec map'
        result.is(spec)
    }

    def 'pre-order-edges one edge'() {
        given: 'graph containing vertex A'
        graph.edge 'A', 'B'

        when: 'pre-order-edges is called with action returning CONTINUE'
        def order = []
        Map result = preOrderEdgesTraversal(graph, [root:'A', colors:[:]]) { from, to, toColor ->
            def map = [:]
            map.from = from
            map.to = to
            map.color = toColor
            order << map
            CONTINUE
        }

        then: 'order is correct with correct edge type'
        order[0].from == 'A'
        order[0].to == 'B'
        order[0].color == WHITE
        order[1].from == 'B'
        order[1].to == 'A'
        order[1].color == GREY
        order.size() == 2

        and: 'returned map of results are correct'
        result == [root:'B', colors:[A:BLACK, B:BLACK], state:CONTINUE]
    }

    def 'pre-order-edges one edge with colors'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        Map result = preOrderEdgesTraversal(graph, [root:'A', colors:graph.makeColorMap()]) { from, to, type ->
            CONTINUE
        }

        then:
        result == [root:'B', colors:[A:BLACK, B:BLACK], state:CONTINUE]
    }

    def 'pre-order-edges one edge stop'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        def order = []
        Map result = preOrderEdgesTraversal(graph, [root:'A', colors:[:]]) { from, to, toColor ->
            Map map = [:]
            map.from = from
            map.to = to
            map.toColor = toColor
            order << map
            STOP
        }

        then: 'action was called with correct params'
        order[0].from == 'A'
        order[0].to == 'B'
        order[0].toColor == WHITE
        order.size() == 1

        and: 'returned map of results are correct'
        result == [root:'A', colors:['A':GREY], state:STOP]

    }

    def 'pre-order-edges one edge stop second'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        def order = []
        def result = preOrderEdgesTraversal(graph, [root:'A', colors:[:]]) { from, to, toColor ->
            Map map = [:]
            map.from = from
            map.to = to
            map.toColor = toColor
            order << map
            if(from == 'B') {
                return STOP
            }
        }

        then: 'action was called with correct params'
        order[0].from == 'A'
        order[0].to == 'B'
        order[0].toColor == WHITE
        order[1].from == 'B'
        order[1].to =='A'
        order[1].toColor == GREY
        order.size() == 2

        and: 'returned map of results are correct'
        result == [root:'B', colors:[A:GREY, B:GREY], state:STOP]
    }
}
