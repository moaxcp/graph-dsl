package graph.type.undirected

import graph.ConfigSpec
import graph.type.EdgeSpec
import graph.type.EdgeSpecFactory

/**
 * Creates {@link EdgeSpec}s for an undirected graph.
 */
class UnDirectedEdgeSpecFactory implements EdgeSpecFactory {

    /**
     * Creates a new {@link EdgeSpec} from spec.
     * @param spec  config spec containing a map and closure for the new {@link EdgeSpec}
     * @return the resulting {@link EdgeSpec}.
     */
    @Override
    EdgeSpec newEdgeSpec(ConfigSpec spec) {
        EdgeSpec espec = new EdgeSpec(spec.map)
        espec.runnerCode spec.closure
        espec
    }
}
