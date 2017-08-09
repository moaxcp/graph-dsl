package graph.type

import graph.ConfigSpec

interface VertexSpecFactory {
    VertexSpec newVertexSpec(Map<String, ?> map)
    VertexSpec newVertexSpec(ConfigSpec spec)
}
