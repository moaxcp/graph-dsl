package graph.type.undirected

import graph.ConfigSpec
import graph.Graph
import graph.type.EdgeSpec
import graph.type.EdgeSpecFactory

/**
 * Creates {@link EdgeSpec}s for an undirected graph.
 */
class UnDirectedEdgeSpecFactory implements EdgeSpecFactory {

    @Override
    EdgeSpec newEdgeSpec(Graph graph, Map<String, ?> map) {
        new EdgeSpec(graph, map)
    }

    /**
     * Creates a new {@link EdgeSpec} from spec.
     * @param spec  config spec containing a map and closure for the new {@link EdgeSpec}
     * @return the resulting {@link EdgeSpec}.
     */
    @Override
    EdgeSpec newEdgeSpec(Graph graph, ConfigSpec spec) {
        new EdgeSpec(graph, spec.map, spec.closure)
    }
}
