package graph.type.weighted

import graph.Graph
import static graph.Graph.graph
import spock.lang.Specification

class WeightedDirectedGraph extends Specification {
    WeightedDirectedGraphType type
    def setup() {
        type = new WeightedDirectedGraphType()
    }

    def 'is weighted'() {
        expect:
        type.isWeighted()
    }

    def 'convert sorts edges by weight'() {
        given: 'a weighted directed graph with three weighted edges'
        Graph graph = graph {
            type 'weighted-directed-graph'
            edge A, B, [weight:2]
            edge C, D, [weight:1]
            edge E, F, [weight:3]
        }

        expect:
        graph.edges*.weight == [1, 2, 3]
    }
}
