package graph

import spock.lang.Specification
import spock.lang.Unroll

import static graph.Graph.graph
import static graph.TraversalAlgorithms.preOrderEdgesTraversal
import static graph.TraversalColor.BLACK
import static graph.TraversalColor.WHITE
import static graph.TraversalColor.GREY
import static graph.TraversalState.CONTINUE

class TraversalAlgorithmsOrderOfPreOrderEdges extends Specification {

    def generateGraphTests() {
        return [
                oneEdge(),
                twoEdges(),
                twoEdgesTree(),
                fiveEdges(),
                threeEdgesTree(),
                sevenEdges(),
                fourEdgesTree()
        ]
    }

    def oneEdge() {
        def graph = graph {
            edge 'A', 'B'
        }
        return ['one edge', graph, [
                ['A', 'B', WHITE],
                ['B', 'A', GREY]]
        ]
    }

    def twoEdges() {
        def graph = graph {
            edge 'A', 'B'
            edge 'B', 'C'
        }
        return ['two edges', graph, [
                ['A', 'B', WHITE],
                ['B', 'A', GREY],
                ['B', 'C', WHITE],
                ['C', 'B', GREY]
        ]]
    }

    def twoEdgesTree() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
        }
        return ['two edges tree', graph, [
                ['A', 'B', WHITE],
                ['B', 'A', GREY],
                ['A', 'C', WHITE],
                ['C', 'A', GREY]
        ]]
    }

    def fiveEdges() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'D'
            //extra edges
            edge 'C', 'B'
            edge 'A', 'D'
        }
        return ['five edges', graph, [
                ['A', 'B', WHITE],
                ['B', 'A', GREY],
                ['B', 'C', WHITE],
                ['C', 'A', GREY],
                ['C', 'D', WHITE],
                ['D', 'C', GREY],
                ['D', 'A', GREY],
                ['C', 'B', GREY],
                ['A', 'C', BLACK],
                ['A', 'D', BLACK]
        ]]
    }

    def threeEdgesTree() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'D'
        }
        return ['three edges tree', graph, [
                ['A', 'B', WHITE],
                ['B', 'A', GREY],
                ['A', 'C', WHITE],
                ['C', 'A', GREY],
                ['C', 'D', WHITE],
                ['D', 'C', GREY]
        ]]
    }

    def sevenEdges() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'B'
            edge 'C', 'D'
            edge 'D', 'A'
            edge 'C', 'E'
            edge 'E', 'B'
        }
        return ['seven edges', graph, [
                ['A', 'B', WHITE],
                ['B', 'A', GREY],
                ['B', 'C', WHITE],
                ['C', 'A', GREY],
                ['C', 'B', GREY],
                ['C', 'D', WHITE],
                ['D', 'C', GREY],
                ['D', 'A', GREY],
                ['C', 'E', WHITE],
                ['E', 'C', GREY],
                ['E', 'B', GREY],
                ['B', 'E', BLACK],
                ['A', 'C', BLACK],
                ['A', 'D', BLACK]
        ]]
    }

    def fourEdgesTree() {
        def graph = graph {
            edge 'A', 'B'
            edge 'A', 'C'
            edge 'C', 'D'
            edge 'C', 'E'
        }
        return ['four edges tree', graph, [
                ['A', 'B', WHITE],
                ['B', 'A', GREY],
                ['A', 'C', WHITE],
                ['C', 'A', GREY],
                ['C', 'D', WHITE],
                ['D', 'C', GREY],
                ['C', 'E', WHITE],
                ['E', 'C', GREY]
        ]]
    }

    @Unroll
    def 'pre-order-edges order: #name'() {
        when: 'pre-order is called on "#name"'
        def order = []
        def result = preOrderEdgesTraversal((Graph) graph, [root:'A', colors:[:]]) { from, to, toColor ->
            order << [from, to, toColor]
            CONTINUE
        }

        then: 'order matches #expected'
        order == expected
        result.colors.values().every { it == BLACK }

        where:
        [name, graph, expected] << generateGraphTests()
    }
}
