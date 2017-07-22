package dsl

import graph.EdgeClassification
import spock.lang.Specification
import graph.Graph

class DepthFirstTraversalSpec extends Specification {
    Graph graph = new Graph()

    def setup() {
        graph.with {
            edge A, B
            edge A, D
            edge A, E
            edge B, D
            edge B, C
            edge D, C
            edge D, E
            edge D, A
        }
    }

    def 'depth first traversal with string root'() {
        given:
        def names = []

        when:
        graph.with {
            depthFirstTraversal {
                root = 'A'
                preorder { vertex ->
                    names << vertex.name
                }
            }
        }

        then:
        names == ['A', 'B', 'D', 'C', 'E']
    }

    def 'depth first traversal with VertexNameSpec root'() {
        given:
        def names = []

        when:
        graph.with {
            depthFirstTraversal {
                root A
                preorder { vertex ->
                    names << vertex.name
                }
            }
        }

        then:
        names == ['A', 'B', 'D', 'C', 'E']
    }

    def 'depth first traversal with string root param'() {
        given:
        def names = []

        when:
        graph.with {
            depthFirstTraversal('A') {
                preorder { vertex ->
                    names << vertex.name
                }
            }
        }

        then:
        names == ['A', 'B', 'D', 'C', 'E']
    }

    def 'depth first traversal with VertexNameSpec root param'() {
        given:
        def names = []

        when:
        graph.with {
            depthFirstTraversal(A) {
                preorder { vertex ->
                    names << vertex.name
                }
            }
        }

        then:
        names == ['A', 'B', 'D', 'C', 'E']
    }
}
