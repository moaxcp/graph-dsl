package graph

/**
 * An enum defining traversal status. A value from this enum can be returned
 * from one of the closures passed to search methods changing the behavior of a traversal.
 */
enum Traversal {
    /**
     * stops the current traversal. Useful in search when a vertex is found to end the traversal early.
     */
    STOP,
    /**
     * continues the current traversal.
     */
    CONTINUE
}
