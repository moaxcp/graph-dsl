package graph.type.directed

import graph.ConfigSpec
import graph.Graph
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
    VertexSpec newVertexSpec(Graph graph, Map<String, ?> map) {
        new DirectedVertexSpec(graph, map)
    }

    /**
     * Creates a new {@link VertexSpec} from spec.
     * @param spec
     * @return
     */
    @Override
    VertexSpec newVertexSpec(Graph graph, ConfigSpec spec) {
        new DirectedVertexSpec(graph, spec.map, spec.closure)
    }
}
