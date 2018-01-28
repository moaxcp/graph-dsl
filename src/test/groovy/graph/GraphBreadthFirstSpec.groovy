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

    def 'can breadthFirstTraversalSpec'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'A'
        }

        when:
        def spec = graph.breadthFirstTraversalSpec {
            visit {
                //do nothing
            }
        }

        then:
        spec.root == 'A'
        spec.colors == ['A' : WHITE]
        spec.visit != null
    }

    def 'breadthFirstTraversalConnected visit'() {
        setup:
        def visitList = []

        def spec = new BreadthFirstTraversalSpec()
        spec.root = 'A'
        spec.colors = graph.makeColorMap()
        spec.visit { vertex ->
            visitList << vertex.key
        }

        when:
        def traversal = graph.breadthFirstTraversalConnected spec

        then:
        traversal != STOP
        spec.colors == [
            A: BLACK, B: BLACK, C: BLACK, D: BLACK, E: BLACK
        ]
        visitList == ['A', 'B', 'D', 'E', 'C']
    }

    def 'root does not exist'() {
        setup:
        def spec = new BreadthFirstTraversalSpec()
        spec.root = 'step1'
        spec.colors = graph.makeColorMap()

        when:
        graph.breadthFirstTraversalConnected spec

        then:
        thrown IllegalArgumentException
    }

    def 'can stop traversal'() {
        setup:
        def spec = new BreadthFirstTraversalSpec()
        spec.root = 'A'
        spec.colors = graph.makeColorMap()
        def visitList = []
        spec.visit { vertex ->
            if(vertex.key == 'E') {
                return STOP
            }
            visitList << vertex.key
        }

        when:
        def traversal = graph.breadthFirstTraversalConnected spec

        then:
        traversal == STOP
        spec.colors == [
                A: GREY, B: GREY, C: WHITE, D: GREY, E: GREY
        ]
        visitList == ['A', 'B', 'D']
    }

    def 'can stop traversal at root'() {
        setup:
        def spec = new BreadthFirstTraversalSpec()
        spec.root = 'A'
        spec.colors = graph.makeColorMap()
        def visitList = []
        spec.visit { vertex ->
            visitList << vertex.key
            if(vertex.key == 'A') {
                return STOP
            }
        }

        when:
        def traversal = graph.breadthFirstTraversalConnected spec

        then:
        traversal == STOP
        spec.colors == [
                A: GREY, B: WHITE, C: WHITE, D: WHITE, E: WHITE
        ]
        visitList == ['A']
    }

    def 'breadthFirstTraversal with closure'() {
        setup:
        def visitList = []

        when:
        def traversal = graph.breadthFirstTraversal {
            visit { vertex ->
                visitList << vertex.key
            }
        }

        then:
        visitList == ['A', 'B', 'D', 'E', 'C']
    }

    def 'breadthFirstTraversal change root'() {
        setup:
        def visitList = []

        when:
        def traversal = graph.breadthFirstTraversal {
            root = 'E'
            visit { vertex ->
                visitList << vertex.key
            }
        }

        then:
        visitList == ['E', 'A', 'D', 'B', 'C']
    }
}
