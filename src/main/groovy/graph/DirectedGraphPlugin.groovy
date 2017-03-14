package graph

/**
 * This plugin makes a Graph behave like a directed graph.
 */
class DirectedGraphPlugin implements Plugin {

    /**
     * The following modifications are made to the graph:
     *
     * members
     * edges - converted to DirectedEdges
     * edgeFactory - changed to a DirectedEdgeFactory
     *
     * methods
     * outEdges - returns out going edges from a given vertex name
     * adjacentEdges - now only returns outgoing edges from the given vertex name
     * @param graph
     * @return
     */
    void apply(Graph graph) {
        graph.@edges = graph.@edges.collect { edge ->
            new DirectedEdge(one:edge.one, two:edge.two)
        } as LinkedHashSet

        graph.edgeFactory = new DirectedEdgeFactory()

        graph.metaClass.outEdges = { name ->
            graph.@edges.findAll {
                name == it.one
            }
        }

        graph.metaClass.adjacentEdges = { name ->
            outEdges name
        }
    }
}
