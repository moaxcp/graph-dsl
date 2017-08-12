package graph.type

import graph.ConfigSpec
import graph.Graph

/**
 * Creates new {@link EdgeSpec}s.
 */
interface EdgeSpecFactory {

    EdgeSpec newEdgeSpec(Graph graph, Map<String, ?> map)
    /**
     * Creates a new {@link EdgeSpec} from spec.
     * @param spec
     * @return
     */
    EdgeSpec newEdgeSpec(Graph graph, ConfigSpec spec)
}
