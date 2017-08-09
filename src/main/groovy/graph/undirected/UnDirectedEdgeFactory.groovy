package graph.undirected

import graph.Edge
import graph.EdgeFactory

/**
 * Creates {@link Edge} objects for {@link Graph}.
 */
class UnDirectedEdgeFactory implements EdgeFactory {

    /**
     * Creates and returns a new {@link graph.Edge} setting on and two to the params.
     * @param one - The name of the first {@link graph.Vertex}.
     * @param two - The name of the second {@link graph.Vertex}.
     * @return the resulting {@link graph.Edge}.
     */
    @Override
    Edge newEdge(String one, String two) {
        new Edge(one:one, two:two)
    }
}
