package graph

interface VertexSpecFactory {
    VertexSpec newVertexSpec(Map<String, ?> map)
    VertexSpec newVertexSpec(ConfigSpec spec)
}
