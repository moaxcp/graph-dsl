package graph

import groovy.transform.PackageScope

/**
 * The results from classifyEdges in Graph.
 */
class EdgeClassification {
    Graph forrest = []
    List<Edge> backEdges = []
    List<Edge> treeEdges = []
    List<Edge> forwardEdges = []
    List<Edge> crossEdges = []

    /**
     * The resulting edge type. Used in addEdge to notify the action closure.
     */
    enum EdgeType {
        /**
         * When the followed edge is GREY
         */
        BACK_EDGE,
        /**
         * When the followed edge is WHITE
         */
        TREE_EDGE,
        /**
         * When the followed edge is BLACK and the connecting vertex is not in the forrest
         */
        FORWARD_EDGE,
        /**
         * When the followed edge is BLACK and the connecting vertex is in the forreest
         */
        CROSS_EDGE
    }

    /**
     * Adds an edge calling action with the classification.
     * @param graph
     * @param edge
     * @param from
     * @param to
     * @param toColor
     * @param action
     */
    @PackageScope
    void addEdge(Graph graph, Edge edge, String from, String to, Graph.TraversalColor toColor, Closure action) {
        def edgeType
        switch(toColor) {
            case Graph.TraversalColor.WHITE:
                forrest.addVertex(graph.vertex(from))
                forrest.addVertex(graph.vertex(to))
                forrest.addEdge(edge)
                treeEdges << edge
                edgeType = EdgeClassification.EdgeType.TREE_EDGE
                break

            case Graph.TraversalColor.GREY:
                backEdges << edge
                edgeType = EdgeClassification.EdgeType.BACK_EDGE
                break

            case Graph.TraversalColor.BLACK:
                if(forrest.vertices[to]) {
                    crossEdges << edge
                    edgeType = EdgeClassification.EdgeType.CROSS_EDGE
                } else {
                    forwardEdges << edge
                    edgeType = EdgeClassification.EdgeType.FORWARD_EDGE
                }
                break

            default:
                throw new IllegalStateException("Edge from $from to $to needs to be WHITE, GREY, or BLACK.")
        }
        action(from, to, edgeType)
    }
}
