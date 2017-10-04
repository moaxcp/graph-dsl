package graph.type.undirected

import graph.ConfigSpec
import graph.Graph

/**
 * Creates new {@link GraphEdgeSpec}s.
 */
interface EdgeSpecFactory {

    GraphEdgeSpec newEdgeSpec(Graph graph, Map<String, ?> map)
    /**
     * Creates a new {@link GraphEdgeSpec} from spec.
     * @param spec
     * @return
     */
    GraphEdgeSpec newEdgeSpec(Graph graph, ConfigSpec spec)
}
