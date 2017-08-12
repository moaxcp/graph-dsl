package graph.type.undirected

import graph.ConfigSpec
import graph.type.VertexSpec
import graph.type.VertexSpecFactory

/**
 * Creates a new {@link VertexSpec} for an undirected graph.
 */
class UnDirectedVertexSpecFactory implements VertexSpecFactory {

    /**
     * Creates a new {@link VertexSpec} from map.
     * @param map
     * @return
     */
    @Override
    VertexSpec newVertexSpec(Map<String, ?> map) {
        new VertexSpec(map)
    }

    /**
     * Creates a new {@link VertexSpec} from spec.
     * @param spec
     * @return
     */
    @Override
    VertexSpec newVertexSpec(ConfigSpec spec) {
        VertexSpec vspec = new VertexSpec(spec.map)
        vspec.runnerCode spec.closure
        vspec
    }
}
