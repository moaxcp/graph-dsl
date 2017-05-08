package graph

/**
 * The base specification for any traversal.
 */
class TraversalSpec {
    /**
     * The root {@link Vertex} to start the traversal from.
     */
    String root

    /**
     * The state of colors to start the traversal from.
     */
    Map colors
}
