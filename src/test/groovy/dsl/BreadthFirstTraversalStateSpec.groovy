package dsl

import static graph.TraversalState.*
import spock.lang.Specification
import graph.Graph

class BreadthFirstTraversalStateSpec extends Specification {
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

    def 'breadth first traversal with string root'() {
        given:
        def names = []
        graph.with {
            breadthFirstTraversal('A') { vertex ->
                names << vertex.id
                CONTINUE
            }
        }

        expect:
        names == ['A', 'B', 'D', 'E', 'C']
    }

    def 'breadth first traversal with string root param'() {
        given:
        def names = []
        graph.with {
            breadthFirstTraversal('A') { vertex ->
                names << vertex.id
                CONTINUE
            }
        }

        expect:
        names == ['A', 'B', 'D', 'E', 'C']
    }
}
