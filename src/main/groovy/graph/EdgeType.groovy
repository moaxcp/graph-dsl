package graph

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