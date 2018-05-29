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
     * The roots of the traversal. Connected traversals start at each of these roots.
     */
    Set roots = [] as Set

    /**
     * The state of colors to start the traversal from.
     */
    Map colors
}
