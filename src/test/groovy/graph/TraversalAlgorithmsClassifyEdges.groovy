package graph

import spock.lang.Specification

import static graph.EdgeType.BACK_EDGE
import static graph.EdgeType.CROSS_EDGE
import static graph.EdgeType.FORWARD_EDGE
import static graph.EdgeType.TREE_EDGE
import static graph.Graph.graph
import static TraversalAlgorithms.classifyEdgesTraversal
import static graph.TraversalColor.BLACK
import static graph.TraversalColor.GREY
import static graph.TraversalState.CONTINUE
import static graph.TraversalState.STOP

class TraversalAlgorithmsClassifyEdges extends Specification {

    Graph graph = graph {}

    def 'classify-edges null spec'() {
        when: 'classify-edges is called with a null spec'
        classifyEdgesTraversal(graph, null, null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'classify-edges empty spec'() {
        when: 'classify-edges is called with an empty spec'
        classifyEdgesTraversal(graph, [:], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'classify-edges missing colors'() {
        when: 'classify-edges is called with missing colors'
        classifyEdgesTraversal(graph, [root:'A'], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'classify-edges missing action'() {
        when: 'classify-edges is called with missing action'
        Map result = classifyEdgesTraversal(graph, [root:'A', colors:[:]], null)
        result.remove('forrest')

        then: 'invalid results are returned'
        result == [root:'A', colors:[A:BLACK], state:CONTINUE]
    }

    def 'classify-edges one edge with missing action'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when: 'classify-edges is called with missing action'
        classifyEdgesTraversal(graph, [root:'A', colors:[:]], null)

        then: 'NullPointerException is thrown'
        thrown NullPointerException
    }

    def 'classify-edges empty graph with valid action'() {
        when: 'classify-edges is called with empty graph and valid action'
        def actionNotCalled = true
        classifyEdgesTraversal(graph, [root:'A', colors:[:]]) {
            actionNotCalled = false
            CONTINUE
        }

        then: 'action is not called'
        actionNotCalled
    }

    def 'classify-edges returns spec' () {
        given: 'spec param'
        Map spec = [root:'A', colors:[:]]

        when: 'classify-edges is called'
        Map result = classifyEdgesTraversal(graph, spec) { }

        then: 'result is original spec map'
        result.is(spec)
    }

    def 'classify-edges one edge'() {
        given: 'graph containing vertex A'
        graph.edge 'A', 'B'

        when: 'classify-edges is called with action returning CONTINUE'
        def order = []
        Map result = classifyEdgesTraversal(graph, [root:'A', colors:[:]]) { from, to, type ->
            def map = [:]
            map.from = from
            map.to = to
            map.type = type
            order << map
            CONTINUE
        }
        def forrest = result.forrest
        result.remove('forrest')

        then: 'order is correct with correct edge type'
        order[0].from == 'A'
        order[0].to == 'B'
        order[0].type == TREE_EDGE
        order[1].from == 'B'
        order[1].to == 'A'
        order[1].type == BACK_EDGE
        order.size() == 2

        and: 'returned map of results are correct'
        result == [root:'B', colors:[A:BLACK, B:BLACK], state:CONTINUE]

        and: 'forrest in returned map is correct'
        forrest.edges.size() == 1
        forrest.edges.first() == new Edge(one:'A', two:'B')
        forrest.vertices.A.key == 'A'
        forrest.vertices.B.key == 'B'
        forrest.vertices.size() == 2
    }

    def 'classify-edges one edge with colors'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        Map result = classifyEdgesTraversal(graph, [root:'A', colors:graph.makeColorMap()]) { from, to, type ->
            CONTINUE
        }
        result.remove('forrest')

        then:
        result == [root:'B', colors:[A:BLACK, B:BLACK], state:CONTINUE]
    }

    def 'classify-edges one edge stop'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        def order = []
        Map result = classifyEdgesTraversal(graph, [root:'A', colors:[:]]) { from, to, type ->
            Map map = [:]
            map.from = from
            map.to = to
            map.type = type
            order << map
            STOP
        }
        def forrest = result.forrest
        result.remove('forrest')

        then: 'action was called with correct params'
        order[0].from == 'A'
        order[0].to == 'B'
        order[0].type == TREE_EDGE
        order.size() == 1

        and: 'returned map of results are correct'
        result == [root:'A', colors:['A':GREY], state:STOP]

        and: 'forrest in returned map is correct'
        forrest.edges.size() == 1
        forrest.edges.first() == new Edge(one:'A', two:'B')
        forrest.vertices.A.key == 'A'
        forrest.vertices.B.key == 'B'
        forrest.vertices.size() == 2

    }

    def 'classify-edges one edge stop second'() {
        given: 'graph has edge A -- B'
        graph.edge 'A', 'B'

        when:
        def order = []
        def result = classifyEdgesTraversal(graph, [root:'A', colors:[:]]) { from, to, type ->
            Map map = [:]
            map.from = from
            map.to = to
            map.type = type
            order << map
            if(from == 'B') {
                return STOP
            }
            CONTINUE
        }
        def forrest = result.forrest
        result.remove('forrest')

        then: 'action was called with correct params'
        order[0].from == 'A'
        order[0].to == 'B'
        order[0].type == TREE_EDGE
        order[1].from == 'B'
        order[1].to =='A'
        order[1].type == BACK_EDGE
        order.size() == 2

        and: 'returned map of results are correct'
        result == [root:'B', colors:[A:GREY, B:GREY], state:STOP]

        and: 'forrest in returned map is correct'
        forrest.edges.size() == 1
        forrest.edges.first() == new Edge(one:'A', two:'B')
        forrest.vertices.A.key == 'A'
        forrest.vertices.B.key == 'B'
        forrest.vertices.size() == 2
    }

    def 'classify-edges directed graph with all edge types'() {
        //example from http://www.cs.cornell.edu/courses/cs2112/2012sp/lectures/lec24/lec24-12sp.html
        given: 'directed graph with all edge types'
        graph.with {
            type 'directed-graph'
            edge 'A', 'B'
            edge 'A', 'D'
            edge 'B', 'C'
            edge 'B', 'D'
            edge 'D', 'E'
            edge 'D', 'C'
            edge 'D', 'A'

            edge 'F', 'G'
            edge 'G', 'D'
        }

        when: 'classify-edges is called on root A'
        def firstTree = []
        Map result = classifyEdgesTraversal(graph, [root:'A', traversalRoot:'A', colors:[:]]) { from, to, type ->
            Map map = [:]
            map.from = from
            map.to = to
            map.type = type
            firstTree << map
            CONTINUE
        }
        and: 'classify-edges is called on root F'
        def secondTree =[]
        result.root = 'F'
        result.traversalRoot = 'F'
        classifyEdgesTraversal(graph, result) { from, to, type ->
            Map map = [:]
            map.from = from
            map.to = to
            map.type = type
            secondTree << map
            CONTINUE
        }

        then: 'action was called with correct params'
        firstTree.size() == 7
        firstTree[0].from == 'A'
        firstTree[0].to == 'B'
        firstTree[0].type == TREE_EDGE
        firstTree[1].from == 'B'
        firstTree[1].to == 'C'
        firstTree[1].type == TREE_EDGE
        firstTree[2].from == 'B'
        firstTree[2].to == 'D'
        firstTree[2].type == TREE_EDGE
        firstTree[3].from == 'D'
        firstTree[3].to == 'E'
        firstTree[3].type == TREE_EDGE
        firstTree[4].from == 'D'
        firstTree[4].to == 'C'
        firstTree[4].type == FORWARD_EDGE
        firstTree[5].from == 'D'
        firstTree[5].to == 'A'
        firstTree[5].type == BACK_EDGE
        firstTree[6].from == 'A'
        firstTree[6].to == 'D'
        firstTree[6].type == FORWARD_EDGE

        secondTree.size() == 2
        secondTree[0].from == 'F'
        secondTree[0].to == 'G'
        secondTree[0].type == TREE_EDGE
        secondTree[1].from == 'G'
        secondTree[1].to == 'D'
        secondTree[1].type == CROSS_EDGE

        and: 'returned results are correct'
        Graph forrest = result.forrest
        result.remove('forrest')
        result == [root:'G', traversalRoot:'F', colors:[A:BLACK, B:BLACK, C:BLACK, D:BLACK, E:BLACK, F:BLACK, G:BLACK], state:CONTINUE]

        and: 'forrest in results are correct'
        forrest.edges.size() == 5
        forrest.edges.first() == new Edge(one:'A', two:'B')
        forrest.vertices.keySet() == ['A', 'B', 'C', 'D', 'E', 'F', 'G'] as Set
        forrest.vertices.size() == 7
    }
}
