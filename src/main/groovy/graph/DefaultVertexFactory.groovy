package graph

/**
 * Factory for creating vertices. This factory returns instances of the
 * base class Vertex.
 */
class DefaultVertexFactory implements VertexFactory {
    @Override
    Vertex newVertex(String name) {
        new Vertex(name:name)
    }
}
