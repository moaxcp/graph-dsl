package graph

/**
 * Factory for creating vertices. This factory returns instances of the
 * base class Vertex.
 */
class DefaultVertexFactory implements VertexFactory {
    /**
     * Creates a new Vertex with the provided name.
     * @param name
     * @return
     */
    @Override
    Vertex newVertex(String name) {
        new Vertex(name:name)
    }
}
