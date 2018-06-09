package graph

import spock.lang.Specification

import static TraversalAlgorithms.postOrderTraversal
import static graph.TraversalColor.BLACK
import static graph.TraversalColor.GREY
import static graph.TraversalState.CONTINUE
import static graph.TraversalState.STOP

class TraversalAlgorithmsPostOrder extends Specification {

    Graph graph = new Graph()

    def 'post-order null spec'() {
        when: 'post-order is called with a null spec'
        postOrderTraversal(graph, null, null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'post-order empty spec'() {
        when: 'post-order is called with an empty spec'
        postOrderTraversal(graph, [:], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'post-order missing colors'() {
        when: 'post-order is called with missing colors'
        postOrderTraversal(graph, [root:'A'], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'post-order missing action'() {
        when: 'post-order is called with missing action'
        Map result = postOrderTraversal(graph, [root:'A', colors:[:]], null)

        then: 'invalid results are returned'
        result == [root:'A', colors:[A:BLACK], state:CONTINUE]
    }

    def 'post-order one vertex with missing action'() {
        given: 'graph has vertex A'
        graph.vertex 'A'

        when: 'post-order is called with missing action'
        postOrderTraversal(graph, [root:'A', colors:[:]], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'post-order empty graph with valid action'() {
        when: 'post-order is called with empty graph and valid action'
        def actionNotCalled = true
        postOrderTraversal(graph, [root:'A', colors:[:]]) {
            actionNotCalled = false
            CONTINUE
        }

        then: 'action is not called'
        actionNotCalled
    }

    def 'post-order returns spec' () {
        given: 'spec param'
        Map spec = [root:'A', colors:[:]]

        when: 'post-order is called'
        Map result = postOrderTraversal(graph, spec) { }

        then: 'result is original spec map'
        result.is(spec)
    }

    def 'post-order one vertex action returns null'() {
        given: 'graph containing vertex A'
        graph.vertex 'A'

        when: 'post-order is called with action returning null'
        postOrderTraversal(graph, [root:'A', colors:[:]]) {
            null
        }

        then: 'NullPointerException is thrown'
        NullPointerException e = thrown()
        e.message == 'action cannot return null TraversalState.'
    }

    def 'post-order one vertex'() {
        given: 'graph containing vertex A'
        graph.vertex 'A'

        when: 'post-order is called with action returning CONTINUE'
        Vertex vertex = null
        Map result = postOrderTraversal(graph, [root:'A', colors:[:]]) {
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

    def 'post-order one vertex stop'() {
        given: 'graph has vertex A'
        graph.vertex 'A'

        when:
        Vertex vertex = null
        Map result = postOrderTraversal(graph, [root:'A', colors:[:]]) {
            vertex = it
            STOP
        }

        then: 'vertex param in action was A'
        vertex.id == 'A'
        and: 'A was marked as frontier'
        result.colors.A == BLACK
        and: 'state is STOP'
        result.state == STOP
        and: 'root is A'
        result.root == 'A'
        and: 'no other entries are in result'
        result == [root:'A', colors:['A':BLACK], state:STOP]
    }

    def 'post-order two vertex stop second'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        def result = postOrderTraversal(graph, [root:'A', colors:[:]]) {
            if(it.id == 'B') {
                return STOP
            }
        }

        then: 'A was marked as frontier'
        result.colors.A == GREY
        and: 'B was marked as visited'
        result.colors.B == BLACK
        and: 'state is STOP'
        result.state == STOP
        and: 'root is B'
        result.root == 'B'
        and: 'no other entries are in result'
        result == [root:'B', colors:[A:GREY, B:BLACK], state:STOP]
    }
}
