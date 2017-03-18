package graph

import groovy.transform.PackageScope

class EdgeClassification {
    Graph forrest = []
    List<Edge> backEdges = []
    List<Edge> treeEdges = []
    List<Edge> forwardEdges = []
    List<Edge> crossEdges = []

    enum EdgeType {
        BACK_EDGE,
        TREE_EDGE,
        FORWARD_EDGE,
        CROSS_EDGE
    }

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
