package graph

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
}
