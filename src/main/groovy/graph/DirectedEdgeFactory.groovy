package graph

/**
 * Factory for creating edges. This class returns instances of
 * Directed Edge.
 */
class DirectedEdgeFactory implements EdgeFactory {

    /**
     * Returns a new DirectedEdge with the given parameters.
     * @param one
     * @param two
     * @return a new DirectedEdge
     */
    @Override
    Edge newEdge(String one, String two) {
        new DirectedEdge(one:one, two:two)
    }
}
