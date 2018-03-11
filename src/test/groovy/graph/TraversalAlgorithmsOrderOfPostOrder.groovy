package graph

import spock.lang.Specification
import spock.lang.Unroll

import static graph.Graph.graph
import static TraversalAlgorithms.postOrderTraversal
import static graph.TraversalColor.BLACK
import static graph.TraversalState.CONTINUE

class TraversalAlgorithmsOrderOfPostOrder extends Specification {

    def generateGraphTests() {
        return [
                generateOneVertex(),
                generateTwoVertices(),
                generateThreeVertices(),
                generateThreeVerticesTree(),
                generateFourVerticesTree(),
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
        return ['two vertices', graph, ['B', 'A']]
    }

    def generateThreeVertices() {
        def graph = graph {
            edge 'A', 'B'
            edge 'B', 'C'
        }
        return ['three vertices', graph, ['C', 'B', 'A']]
    }

    def generateThreeVerticesTree() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
        }
        return ['three vertices tree', graph, ['B', 'C', 'A']]
    }

    def generateFourVerticesTree() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'D'
        }
        return ['four vertices tree', graph, ['B', 'D', 'C', 'A']]
    }

    def generateFiveVerticesTree() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'D'
            edge 'C', 'E'
        }
        return ['five vertices trees', graph, ['B', 'D', 'E', 'C', 'A']]
    }

    @Unroll
    def 'post-order connected vertices order: #name'() {
        when: 'post-order is called on "#name"'
        def order = []
        def result = postOrderTraversal((Graph) graph, [root:'A', colors:[:]]) {
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
