package graph

/**
 * Creates {@link Edge} objects for {@link Graph}.
 */
class UnDirectedEdgeFactory implements EdgeFactory {

    /**
     * Creates and returns a new {@link Edge} setting on and two to the params.
     * @param one - The name of the first {@link Vertex}.
     * @param two - The name of the second {@link Vertex}.
     * @return the resulting {@link Edge}.
     */
    @Override
    Edge newEdge(String one, String two) {
        new Edge(one:one, two:two)
    }
}
