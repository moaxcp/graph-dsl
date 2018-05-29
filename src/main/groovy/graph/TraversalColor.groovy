package graph

/**
 * Defines the color for a vertex when traversing.
 */
enum TraversalColor {
    /**
     * an undiscovered vertex
     */
    WHITE,
    /**
     * a discovered vertex that still needs work
     */
    GREY,
    /**
     * a vertex that the algorithm is done with
     */
    BLACK
}
