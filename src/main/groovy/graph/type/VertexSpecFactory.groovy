package graph.type

import graph.ConfigSpec
import graph.Graph

/**
 * Creates new {@link VertexSpec}s for a graph.
 */
interface VertexSpecFactory {

    /**
     * Creates a new {@link VertexSpec} from map.
     * @param map
     * @return
     */
    VertexSpec newVertexSpec(Graph graph, Map<String, ?> map)

    /**
     * Creates a new {@link VertexSpec} from spec.
     * @param spec
     * @return
     */
    VertexSpec newVertexSpec(Graph graph, ConfigSpec spec)
}
