package graph

/**
 * Used to create edges.
 */
interface EdgeFactory {
    /**
     * returns a new Edge
     * @param one
     * @param two
     * @return
     */
    Edge newEdge(String one, String two)
}
