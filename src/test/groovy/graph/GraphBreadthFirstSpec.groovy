package graph

import spock.lang.Specification
import static TraversalColor.*
import static Traversal.*

class GraphBreadthFirstSpec extends Specification {

    def graph = new Graph()

    def setup() {
        graph.with {
            vertex 'A'
            vertex 'B'
            vertex 'C'
            vertex 'D'
            vertex 'E'
            edge 'A', 'B'
            edge 'A', 'D'
            edge 'A', 'E'
            edge 'B', 'D'
            edge 'B', 'C'
            edge 'D', 'C'
            edge 'D', 'E'
            edge 'D', 'A'
        }
    }

    def 'breadthFirstTraversalConnected visit'() {
        setup:
        def visitList = []
        def colors = graph.makeColorMap()

        when:
        def traversal = graph.breadthFirstTraversalConnected('A', colors) { vertex ->
            visitList << vertex.key
            CONTINUE
        }

        then:
        traversal != STOP
        colors == [
            A: BLACK, B: BLACK, C: BLACK, D: BLACK, E: BLACK
        ]
        visitList == ['A', 'B', 'D', 'E', 'C']
    }

    def 'root does not exist'() {
        when:
        graph.breadthFirstTraversalConnected('step1', graph.makeColorMap()) {

        }

        then:
        thrown IllegalArgumentException
    }

    def 'can stop traversal'() {
        setup:
        def visitList = []
        def colors = graph.makeColorMap()

        when:
        def traversal = graph.breadthFirstTraversalConnected('A', colors) { vertex ->
            if(vertex.key == 'E') {
                return STOP
            }
            visitList << vertex.key
            return CONTINUE
        }

        then:
        traversal == STOP
        colors == [
                A: GREY, B: GREY, C: WHITE, D: GREY, E: GREY
        ]
        visitList == ['A', 'B', 'D']
    }

    def 'can stop traversal at root'() {
        setup:
        def colors = graph.makeColorMap()
        def visitList = []

        when:
        def traversal = graph.breadthFirstTraversalConnected('A', colors) { vertex ->
            visitList << vertex.key
            if(vertex.key == 'A') {
                return STOP
            }
            return CONTINUE
        }

        then:
        traversal == STOP
        colors == [
                A: GREY, B: WHITE, C: WHITE, D: WHITE, E: WHITE
        ]
        visitList == ['A']
    }
}
