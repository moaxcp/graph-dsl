package graph.type.directed

import graph.ConfigSpec
import graph.Graph
import graph.type.undirected.GraphVertexSpec
import graph.type.undirected.VertexSpecFactory

/**
 * Creates a new {@link graph.type.undirected.GraphVertexSpec} for a directed graph.
 */
class DirectedVertexSpecFactory implements VertexSpecFactory {

    /**
     * Creates a new {@link GraphVertexSpec} from map.
     * @param map
     * @return
     */
    @Override
    GraphVertexSpec newVertexSpec(Graph graph, Map<String, ?> map) {
        new DirectedVertexSpec(graph, map)
    }

    /**
     * Creates a new {@link GraphVertexSpec} from spec.
     * @param spec
     * @return
     */
    @Override
    GraphVertexSpec newVertexSpec(Graph graph, ConfigSpec spec) {
        new DirectedVertexSpec(graph, spec.map, spec.closure)
    }
}
