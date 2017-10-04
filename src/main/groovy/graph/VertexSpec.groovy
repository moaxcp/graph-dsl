package graph

abstract class VertexSpec {
    Graph graph

    protected void addVertex(Vertex vertex) {
        graph.addVertex(vertex)
    }
}
