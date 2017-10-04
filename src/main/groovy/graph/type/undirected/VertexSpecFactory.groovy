package graph.type.undirected

import graph.ConfigSpec
import graph.Graph

/**
 * Creates new {@link GraphVertexSpec}s for a graph.
 */
interface VertexSpecFactory {

    /**
     * Creates a new {@link GraphVertexSpec} from map.
     * @param map
     * @return
     */
    GraphVertexSpec newVertexSpec(Graph graph, Map<String, ?> map)

    /**
     * Creates a new {@link GraphVertexSpec} from spec.
     * @param spec
     * @return
     */
    GraphVertexSpec newVertexSpec(Graph graph, ConfigSpec spec)
}
