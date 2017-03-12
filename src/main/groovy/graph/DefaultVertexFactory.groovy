package graph

/**
 * Factory for creating vertices.
 */
class DefaultVertexFactory implements VertexFactory {
    @Override
    Vertex newVertex(String name) {
        new Vertex(name:name)
    }
}
