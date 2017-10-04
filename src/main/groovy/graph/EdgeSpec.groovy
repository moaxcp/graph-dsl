package graph

abstract class EdgeSpec {
    Graph graph

    protected void addEdge(Edge edge) {
        graph.addEdge(edge)
    }
}
