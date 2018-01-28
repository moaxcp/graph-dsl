package nondsl

import graph.Graph
import spock.lang.Specification

class BreadthFirstTraversalSpec extends Specification {
    Graph graph = new Graph()

    def setup() {
        graph.with {
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

    def 'breadth first traversal with root'() {
        given:
        def names = []
        graph.breadthFirstTraversal {
            root = 'A'
            visit { vertex ->
                names << vertex.key
            }
        }

        expect:
        names == ['A', 'B', 'D', 'E', 'C']
    }
}
