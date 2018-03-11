package graph

import spock.lang.Specification

import static graph.Graph.graph
import static graph.TraversalAlgorithms.breadthFirstTraversal
import static graph.TraversalColor.BLACK
import static graph.TraversalColor.GREY
import static graph.TraversalState.CONTINUE
import static graph.TraversalState.STOP

class TraversalAlgorithmsBreadthFirst extends Specification {

    Graph graph = graph {}

    def 'breadth-first-traversal null spec'() {
        when: 'breadth-first-traversal is called with a null spec'
        breadthFirstTraversal(graph, null, null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'breadth-first-traversal empty spec'() {
        when: 'breadth-first-traversal is called with an empty spec'
        breadthFirstTraversal(graph, [:], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'breadth-first-traversal missing colors'() {
        when: 'breadth-first-traversal is called with missing colors'
        breadthFirstTraversal(graph, [root:'A'], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'breadth-first-traversal missing action'() {
        when: 'breadth-first-traversal is called with missing action'
        breadthFirstTraversal(graph, [root:'A', colors:[:]], null)

        then: 'invalid results are returned'
        thrown NullPointerException
    }

    def 'breadth-first-traversal one vertex with missing action'() {
        given: 'graph has vertex A'
        graph.vertex 'A'

        when: 'breadth-first-traversal is called with missing action'
        breadthFirstTraversal(graph, [root:'A', colors:[:]], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'breadth-first-traversal empty graph with valid action'() {
        when: 'breadth-first-traversal is called with empty graph and valid action'
        def actionNotCalled = true
        breadthFirstTraversal(graph, [root:'A', colors:[:]]) {
            actionNotCalled = false
            CONTINUE
        }

        then: 'action is not called'
        actionNotCalled
    }

    def 'breadth-first-traversal returns spec' () {
        given: 'spec param'
        Map spec = [root:'A', colors:[:]]

        when: 'breadth-first-traversal is called'
        Map result = breadthFirstTraversal(graph, spec) { }

        then: 'result is original spec map'
        result.is(spec)
    }

    def 'breadth-first-traversal one vertex'() {
        given: 'graph containing vertex A'
        graph.vertex 'A'

        when: 'breadth-first-traversal is called with action returning CONTINUE'
        Vertex vertex = null
        Map result = breadthFirstTraversal(graph, [root:'A', colors:[:]]) {
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

    def 'breadth-first-traversal one vertex stop'() {
        given: 'graph has vertex A'
        graph.vertex 'A'

        when:
        Vertex vertex = null
        Map result = breadthFirstTraversal(graph, [root:'A', colors:[:]]) {
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

    def 'breadth-first-traversal two vertex stop second'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        def result = breadthFirstTraversal(graph, [root:'A', colors:[:]]) {
            if(it.key == 'B') {
                return STOP
            }
        }

        then: 'returned map of results are correct'
        result == [root:'B', colors:[A:GREY, B:GREY], state:STOP]
    }
}
