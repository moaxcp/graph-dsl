package graph

import spock.lang.Specification
import spock.lang.Unroll

import static graph.Graph.graph
import static graph.TraversalAlgorithms.preOrderTraversal
import static graph.TraversalColor.BLACK
import static graph.TraversalState.CONTINUE

class TraversalAlgorithmsOrderOfPreOrder extends Specification {

    def generateGraphTests() {
        return [
                generateOneVertex(),
                generateTwoVertices(),
                generateThreeVertices(),
                generateThreeVerticesTree(),
                generateFourVertices(),
                generateFourVerticesTree(),
                generateFiveVertices(),
                generateFiveVerticesTree()
        ]
    }

    def generateOneVertex() {
        def graph = graph {
            vertex 'A'
        }

        return ['one vertex', graph, ['A']]
    }

    def generateTwoVertices() {
        def graph = graph {
            edge 'A', 'B'
        }
        return ['two vertices', graph, ['A', 'B']]
    }

    def generateThreeVertices() {
        def graph = graph {
            edge 'A', 'B'
            edge 'B', 'C'
        }
        return ['three vertices', graph, ['A', 'B', 'C']]
    }

    def generateThreeVerticesTree() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
        }
        return ['three vertices tree', graph, ['A', 'B', 'C']]
    }

    def generateFourVertices() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'D'
            //extra edges
            edge 'C', 'B'
            edge 'A', 'D'
        }
        return ['four vertices', graph, ['A', 'B', 'C', 'D']]
    }

    def generateFourVerticesTree() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'D'
        }
        return ['four vertices tree', graph, ['A', 'B', 'C', 'D']]
    }

    def generateFiveVertices() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'B'
            edge 'C', 'D'
            edge 'D', 'A'
            edge 'C', 'E'
            edge 'E', 'B'
        }
        return ['five vertices', graph, ['A', 'B', 'C', 'D', 'E']]
    }

    def generateFiveVerticesTree() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'D'
            edge 'C', 'E'
        }
        return ['five vertices tree', graph, ['A', 'B', 'C', 'D', 'E']]
    }

    @Unroll
    def 'pre-order connected vertices order: #name'() {
        when: 'pre-order is called on "#name"'
        def order = []
        def result = preOrderTraversal((Graph) graph, [root:'A', colors:[:]]) {
            order << it.key
            CONTINUE
        }

        then: 'order matches #expected'
        order == expected
        result.colors.values().every { it == BLACK }

        where:
        [name, graph, expected] << generateGraphTests()
    }
}
