package graph

abstract class EdgeSpec {
    Graph graph

    EdgeSpec(Graph graph) {
        if (this.graph) {
            throw new IllegalArgumentException('Graph already set.')
        }
        this.graph = graph
    }

    protected void addEdge(Edge edge) {
        graph.addEdge(edge)
    }

    abstract Edge apply()
}
