package graph

class DefaultVertexFactory implements VertexFactory {
    @Override
    Vertex newVertex(String name) {
        return new Vertex(name: name)
    }
}
