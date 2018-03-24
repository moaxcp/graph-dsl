package graph

import static graph.Graph.graph

class TraversalAlgorithmsConnectedComponentDirected extends TraversalAlgorithmsConnectedComponent {

    def setup() {
        graph = graph {
            type 'directed-graph'
        }
    }
}
