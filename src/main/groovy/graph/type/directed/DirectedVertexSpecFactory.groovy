package graph.type.directed

import graph.ConfigSpec
import graph.type.VertexSpec
import graph.type.VertexSpecFactory

/**
 * Creates a new {@link VertexSpec} for a directed graph.
 */
class DirectedVertexSpecFactory implements VertexSpecFactory {

    /**
     * Creates a new {@link VertexSpec} from map.
     * @param map
     * @return
     */
    @Override
    VertexSpec newVertexSpec(Map<String, ?> map) {
        new DirectedVertexSpec(map)
    }

    /**
     * Creates a new {@link VertexSpec} from spec.
     * @param spec
     * @return
     */
    @Override
    VertexSpec newVertexSpec(ConfigSpec spec) {
        VertexSpec vspec = new DirectedVertexSpec(spec.map)
        vspec.runnerCode spec.closure
        vspec
    }
}
