package graph

/**
 * Specification class that helps vertex methods in {@link Graph} objects. GraphVertexSpec is used to collect the
 * details of an update or create.
 */
abstract class VertexSpec {
    Graph graph

    protected VertexSpec(Graph graph) {
        this.graph = graph
    }

    protected void addVertex(Vertex vertex) {
        graph.addVertex(vertex)
    }

    abstract Vertex apply()
}
