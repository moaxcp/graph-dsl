package graph.type

import graph.ConfigSpec

/**
 * Creates new {@link VertexSpec}s for a graph.
 */
interface VertexSpecFactory {

    /**
     * Creates a new {@link VertexSpec} from map.
     * @param map
     * @return
     */
    VertexSpec newVertexSpec(Map<String, ?> map)

    /**
     * Creates a new {@link VertexSpec} from spec.
     * @param spec
     * @return
     */
    VertexSpec newVertexSpec(ConfigSpec spec)
}
