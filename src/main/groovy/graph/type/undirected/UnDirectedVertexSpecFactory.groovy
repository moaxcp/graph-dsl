package graph.type.undirected

import graph.ConfigSpec
import graph.Graph
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
    VertexSpec newVertexSpec(Graph graph, Map<String, ?> map) {
        new VertexSpec(graph, map)
    }

    /**
     * Creates a new {@link VertexSpec} from spec.
     * @param spec
     * @return
     */
    @Override
    VertexSpec newVertexSpec(Graph graph, ConfigSpec spec) {
        new VertexSpec(graph, spec.map, spec.closure)
    }
}
