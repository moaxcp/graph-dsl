package graph

import spock.lang.Specification

import static graph.TraversalAlgorithms.connectedComponentTraversal
import static graph.TraversalColor.BLACK
import static graph.TraversalColor.GREY
import static graph.TraversalState.CONTINUE
import static graph.TraversalState.STOP

class TraversalAlgorithmsConnectedComponent extends Specification {

    Graph graph

    def setup() {
        graph = new Graph()
    }

    def 'connected-component null spec'() {
        when: 'connected-component is called with a null spec'
        connectedComponentTraversal(graph, null, null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'connected-component empty spec'() {
        when: 'connected-component is called with an empty spec'
        connectedComponentTraversal(graph, [:], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'connected-component missing colors'() {
        when: 'connected-component is called with missing colors'
        connectedComponentTraversal(graph, [root:'A'], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'connected-component missing action'() {
        when: 'connected-component is called with missing action'
        Map result = connectedComponentTraversal(graph, [root:'A', colors:[:]], null)

        then: 'invalid results are returned'
        result == [root:'A', colors:[A:BLACK], state:CONTINUE]
    }

    def 'connected-component one vertex with missing action'() {
        given: 'graph has vertex A'
        graph.vertex 'A'

        when: 'connected-component is called with missing action'
        connectedComponentTraversal(graph, [root:'A', colors:[:]], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'connected-component empty graph with valid action'() {
        when: 'connected-component is called with empty graph and valid action'
        def actionNotCalled = true
        connectedComponentTraversal(graph, [root:'A', colors:[:]]) { component, it ->
            actionNotCalled = false
            CONTINUE
        }

        then: 'action is not called'
        actionNotCalled
    }

    def 'connected-component returns spec' () {
        given: 'spec param'
        Map spec = [root:'A', colors:[:]]

        when: 'connected-component is called'
        Map result = connectedComponentTraversal(graph, spec) { }

        then: 'result is original spec map'
        result.is(spec)
    }

    def 'connected-component one vertex action returns null'() {
        given: 'graph containing vertex A'
        graph.vertex 'A'

        when: 'connected-component is called with action returning null'
        connectedComponentTraversal(graph, [root:'A', colors:[:]]) { component, it ->
            null
        }

        then: 'NullPointerException is thrown'
        NullPointerException e = thrown()
        e.message == 'action cannot return null TraversalState.'
    }

    def 'connected-component one vertex'() {
        given: 'graph containing vertex A'
        graph.vertex 'A'

        when: 'connected-component is called with action returning CONTINUE'
        Vertex vertex = null
        Map result = connectedComponentTraversal(graph, [root:'A', colors:[:]]) { component, it ->
            vertex = it
            CONTINUE
        }

        then: 'vertex param in action was A'
        vertex.key == 'A'
        and: 'A was marked as visited'
        result.colors.A == BLACK
        and: 'state is CONTINUE'
        result.state == CONTINUE
        and: 'root is A'
        result.root == 'A'
        and: 'no other entries are in result'
        result == [root:'A', colors:['A':BLACK], state:CONTINUE]
    }

    def 'connected-component one vertex stop'() {
        given: 'graph has vertex A'
        graph.vertex 'A'

        when:
        Vertex vertex = null
        Map result = connectedComponentTraversal(graph, [root:'A', colors:[:]]) { component, it ->
            vertex = it
            STOP
        }

        then: 'vertex param in action was A'
        vertex.key == 'A'
        and: 'A was marked as frontier'
        result.colors.A == GREY
        and: 'state is STOP'
        result.state == STOP
        and: 'root is A'
        result.root == 'A'
        and: 'no other entries are in result'
        result == [root:'A', colors:['A':GREY], state:STOP]
    }

    def 'connected-component two vertex stop second'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        def result = connectedComponentTraversal(graph, [root:'A', colors:[:]]) { component, it ->
            if(it.key == 'B') {
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
