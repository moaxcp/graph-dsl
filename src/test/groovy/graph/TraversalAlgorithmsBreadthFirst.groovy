package graph

import spock.lang.Specification

import static graph.TraversalAlgorithms.breadthFirstTraversal
import static graph.TraversalColor.BLACK
import static graph.TraversalColor.GREY
import static graph.TraversalState.CONTINUE
import static graph.TraversalState.STOP
import static graph.Graph.graph
class TraversalAlgorithmsBreadthFirst extends Specification {

    Graph myGraph = graph {}

    def 'breadth-first-traversal null spec'() {
        when: 'breadth-first-traversal is called with a null spec'
        breadthFirstTraversal(myGraph, null, null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'breadth-first-traversal empty spec'() {
        when: 'breadth-first-traversal is called with an empty spec'
        breadthFirstTraversal(myGraph, [:], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'breadth-first-traversal missing colors'() {
        when: 'breadth-first-traversal is called with missing colors'
        breadthFirstTraversal(myGraph, [root:'A'], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'breadth-first-traversal missing action'() {
        when: 'breadth-first-traversal is called with missing action'
        breadthFirstTraversal(myGraph, [root:'A', colors:[:]], null)

        then: 'invalid results are returned'
        thrown NullPointerException
    }

    def 'breadth-first-traversal one vertex with missing action'() {
        given: 'graph has vertex A'
        myGraph.vertex 'A'

        when: 'breadth-first-traversal is called with missing action'
        breadthFirstTraversal(myGraph, [root:'A', colors:[:]], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'breadth-first-traversal empty graph with valid action'() {
        when: 'breadth-first-traversal is called with empty graph and valid action'
        def actionNotCalled = true
        breadthFirstTraversal(myGraph, [root:'A', colors:[:]]) {
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
        Map result = breadthFirstTraversal(myGraph, spec) { }

        then: 'result is original spec map'
        result.is(spec)
    }

    def 'breadth-first-traversal one vertex action returns null'() {
        given: 'graph containing vertex A'
        myGraph.vertex 'A'

        when: 'breadth-first-traversal is called with action returning null'
        breadthFirstTraversal(myGraph, [root:'A', colors:[:]]) {
            null
        }

        then: 'NullPointerException is thrown'
        NullPointerException e = thrown()
        e.message == 'action cannot return null TraversalState.'
    }

    def 'breadth-first-traversal two vertices action returns null on second'() {
        given: 'graph containing edge A -- B'
        myGraph.edge 'A', 'B'

        when: 'breadth-first-traversal is called with action returning null on B'
        breadthFirstTraversal(myGraph, [root:'A', colors:[:]]) {
            if(it.id == 'B') {
                return null
            }
            CONTINUE
        }

        then: 'NullPointerException is thrown'
        NullPointerException e = thrown()
        e.message == 'action cannot return null TraversalState.'
    }

    def 'breadth-first-traversal one vertex'() {
        given: 'graph containing vertex A'
        myGraph.vertex 'A'

        when: 'breadth-first-traversal is called with action returning CONTINUE'
        Vertex vertex = null
        Map result = breadthFirstTraversal(myGraph, [root:'A', colors:[:]]) {
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

    def 'breadth-first-traversal one vertex stop'() {
        given: 'graph has vertex A'
        myGraph.vertex 'A'

        when:
        Vertex vertex = null
        Map result = breadthFirstTraversal(myGraph, [root:'A', colors:[:]]) {
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

    def 'breadth-first-traversal two vertex stop second'() {
        given: 'graph has edge A -- B'
        myGraph.edge 'A', 'B'

        when:
        def result = breadthFirstTraversal(myGraph, [root:'A', colors:[:]]) {
            if(it.id == 'B') {
                return STOP
            }
            CONTINUE
        }

        then: 'returned map of results are correct'
        result == [root:'B', colors:[A:GREY, B:GREY], state:STOP]
    }
}
