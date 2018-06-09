package graph

import spock.lang.Specification

import static TraversalAlgorithms.preOrderTraversal
import static graph.TraversalColor.BLACK
import static graph.TraversalColor.GREY
import static graph.TraversalState.CONTINUE
import static graph.TraversalState.STOP

class TraversalAlgorithmsPreOrder extends Specification {

    Graph graph = new Graph()

    def 'pre-order null spec'() {
        when: 'pre-order is called with a null spec'
        preOrderTraversal(graph, null, null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'pre-order empty spec'() {
        when: 'pre-order is called with an empty spec'
        preOrderTraversal(graph, [:], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'pre-order missing colors'() {
        when: 'pre-order is called with missing colors'
        preOrderTraversal(graph, [root:'A'], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'pre-order missing action'() {
        when: 'pre-order is called with missing action'
        Map result = preOrderTraversal(graph, [root:'A', colors:[:]], null)

        then: 'invalid results are returned'
        result == [root:'A', colors:[A:BLACK], state:CONTINUE]
    }

    def 'pre-order one vertex with missing action'() {
        given: 'graph has vertex A'
        graph.vertex 'A'

        when: 'pre-order is called with missing action'
        preOrderTraversal(graph, [root:'A', colors:[:]], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'pre-order empty graph with valid action'() {
        when: 'pre-order is called with empty graph and valid action'
        def actionNotCalled = true
        preOrderTraversal(graph, [root:'A', colors:[:]]) {
            actionNotCalled = false
            CONTINUE
        }

        then: 'action is not called'
        actionNotCalled
    }

    def 'pre-order returns spec' () {
        given: 'spec param'
        Map spec = [root:'A', colors:[:]]

        when: 'pre-order is called'
        Map result = preOrderTraversal(graph, spec) { }

        then: 'result is original spec map'
        result.is(spec)
    }

    def 'pre-order one vertex action returns null'() {
        given: 'graph containing vertex A'
        graph.vertex 'A'

        when: 'pre-order is called with action returning null'
        preOrderTraversal(graph, [root:'A', colors:[:]]) {
            null
        }

        then: 'NullPointerException is thrown'
        NullPointerException e = thrown()
        e.message == 'action cannot return null TraversalState.'
    }

    def 'pre-order one vertex'() {
        given: 'graph containing vertex A'
        graph.vertex 'A'

        when: 'pre-order is called with action returning CONTINUE'
        Vertex vertex = null
        Map result = preOrderTraversal(graph, [root:'A', colors:[:]]) {
            vertex = it
            CONTINUE
        }

        then: 'vertex param in action was A'
        vertex.id == 'A'
        and: 'A was marked as visited'
        result.colors.A == BLACK
        and: 'state is CONTINUE'
        result.state == CONTINUE
        and: 'root is A'
        result.root == 'A'
        and: 'no other entries are in result'
        result == [root:'A', colors:['A':BLACK], state:CONTINUE]
    }

    def 'pre-order one vertex stop'() {
        given: 'graph has vertex A'
        graph.vertex 'A'

        when:
        Vertex vertex = null
        Map result = preOrderTraversal(graph, [root:'A', colors:[:]]) {
            vertex = it
            STOP
        }

        then: 'vertex param in action was A'
        vertex.id == 'A'
        and: 'A was marked as frontier'
        result.colors.A == GREY
        and: 'state is STOP'
        result.state == STOP
        and: 'root is A'
        result.root == 'A'
        and: 'no other entries are in result'
        result == [root:'A', colors:['A':GREY], state:STOP]
    }

    def 'pre-order two vertex stop second'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        def result = preOrderTraversal(graph, [root:'A', colors:[:]]) {
            if(it.id == 'B') {
                return STOP
            }
            CONTINUE
        }

        then: 'A was marked as frontier'
        result.colors.A == GREY
        and: 'B was marked as frontier'
        result.colors.B == GREY
        and: 'state is STOP'
        result.state == STOP
        and: 'root is B'
        result.root == 'B'
        and: 'no other entries are in result'
        result == [root:'B', colors:[A:GREY, B:GREY], state:STOP]
    }
}
