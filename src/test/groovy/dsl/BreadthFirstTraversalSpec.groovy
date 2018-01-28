package dsl

import spock.lang.Specification
import graph.Graph

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

    def 'breadth first traversal with string root'() {
        given:
        def names = []
        graph.with {
            breadthFirstTraversal {
                root = 'A'
                visit { vertex ->
                    names << vertex.key
                }
            }
        }

        expect:
        names == ['A', 'B', 'D', 'E', 'C']
    }

    def 'breadth first traversal with string root param'() {
        given:
        def names = []
        graph.with {
            breadthFirstTraversal('A') {
                visit { vertex ->
                    names << vertex.key
                }
            }
        }

        expect:
        names == ['A', 'B', 'D', 'E', 'C']
    }
}
